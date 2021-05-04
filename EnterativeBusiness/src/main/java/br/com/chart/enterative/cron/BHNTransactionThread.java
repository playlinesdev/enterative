package br.com.chart.enterative.cron;

import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.event.BHNTransactionThreadEvent;
import br.com.chart.enterative.service.request.BHNRequestService;
import br.com.chart.enterative.vo.bhn.BHNRequestContainer;
import br.com.chart.enterative.vo.bhn.BHNResponseContainer;
import br.com.chart.enterative.vo.bhn.BHNTransactionThreadVO;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Responsavel por utilizar o ClientREST para transmitir a Transacao
 *
 * @author Cristhiano Roberto
 *
 */
public class BHNTransactionThread extends Thread {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BHNRequestService bhnRequestService;

    private BHNTransactionThreadVO threadVO;
    private Resource activeResource;

    public BHNTransactionThread(String nome) {
        super(nome);
    }

    @Override
    public void run() {
        try {
            if (Objects.nonNull(threadVO)) {
                BHNResponseContainer resposta = null;
                ACTIVATION_DECISION decisao = ACTIVATION_DECISION.TIMEOUT;
                String endpoint;

                if (!threadVO.isReversal()) {
                    //Busca Recurso de envio do Singleton preenchido pelo ControlaEcho
                    threadVO.getBhnTransaction().setResource(this.activeResource);
                    endpoint = this.activeResource.getUrl();

                    BHNActivation bhnActivation = threadVO.getBhnTransaction().getBhnActivation();
                    log.info(String.format("Transmitindo [%s] TTL Ativacao [%s] TTL Desfazimento [%s]", bhnActivation.getExternalCode(), bhnActivation.getTtlActivation(), bhnActivation.getTtlReversal()));
                    log.debug("Transmitindo " + threadVO.getId() + "/" + threadVO.getCounter());
                } else {
                    //Troca o Recurso de cancelamento para o mesmo servidor de envio.
                    endpoint = threadVO.getBhnTransaction().getResource().getUrl();

                    BHNActivation ativacao = threadVO.getBhnTransaction().getBhnActivation();
                    log.info(String.format("Cancelando [%s] TTL Ativacao [%s] TTL Desfazimento [%s]", ativacao.getExternalCode(), ativacao.getTtlActivation(), ativacao.getTtlReversal()));
                    log.debug("Desfazendo " + threadVO.getId() + "/" + threadVO.getCounter());
                }

                BHNRequestContainer requestContainer = this.bhnRequestService.convert(threadVO.getBhnTransaction());

                try {
                    resposta = this.bhnRequestService.send(requestContainer, endpoint);
                } catch (Exception e) {
                    log.error("Erro ao enviar transação: " + e.getMessage());
                    e.printStackTrace();
                }

                if (Objects.nonNull(resposta)) {
                    String responseCode = resposta.getResponse().getTransaction().getResponseCode();
                    if (Objects.nonNull(responseCode)) {
                        switch (responseCode) {
                            case "00":
                                decisao = ACTIVATION_DECISION.APPROVED;
                                break;
                            case "15":
                            case "74":
                                decisao = ACTIVATION_DECISION.SOFT;
                                break;
                            default:
                                decisao = ACTIVATION_DECISION.HARD;
                                break;
                        }
                    }
                }

                //Verifica se o TTL da ativacao chegou a seu fim para mudar a Decisao
                if (threadVO.getBhnTransaction().getBhnActivation().getTtlActivation() <= 0) {
                    if (threadVO.isReversal() && ACTIVATION_DECISION.APPROVED == decisao) {
                        decisao = ACTIVATION_DECISION.NONPROCESSED;
                    }
                }

                if (threadVO.getBhnTransaction().getBhnActivation().getTtlReversal() <= 0) {
                    // TODO: GERA ALERTA POIS VAI COBRAR DA CHART INDEVIDAMENTE
                    decisao = ACTIVATION_DECISION.NONPROCESSED;
                }

                log.info("DECISAO: " + decisao);

                if (Objects.nonNull(threadVO.getCallback())) {
                    final BHNTransactionThreadEvent event = new BHNTransactionThreadEvent(threadVO.getId(), resposta, decisao, threadVO);
                    threadVO.getCallback().stream().forEach(l -> l.notify(event));
                }
                threadVO = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isReversal() {
        if (Objects.nonNull(this.threadVO)) {
            return this.threadVO.isReversal();
        } else {
            return false;
        }
    }

    public void setActiveResource(Resource activeResource) {
        this.activeResource = activeResource;
    }

    public void setThreadVO(BHNTransactionThreadVO threadVO) {
        this.threadVO = threadVO;
    }
}
