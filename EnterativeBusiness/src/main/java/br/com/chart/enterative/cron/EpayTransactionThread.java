package br.com.chart.enterative.cron;

import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.event.EpayTransactionThreadEvent;
import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.vo.epay.EpayDoTransactionUPResult;
import br.com.chart.enterative.vo.epay.EpayTransactionThreadVO;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EpayTransactionThread extends Thread {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private EpayActivationProcess activationProcess;

    private EpayTransactionThreadVO threadVO;
    private Resource activeResource;

    public EpayTransactionThread(String nome) {
        super(nome);
    }
    
    @Override
    public void run() {
        if (Objects.nonNull(threadVO)) {
            ACTIVATION_DECISION decisao = ACTIVATION_DECISION.TIMEOUT;

            threadVO.getTransaction().setResource(this.activeResource);

            EpayActivation activation = threadVO.getTransaction().getActivation();
            log.info(String.format("Transmitindo [%s]", activation.getExternalCode()));
            log.debug("Transmitindo " + threadVO.getId() + "/" + threadVO.getCounter());
            
            EpayDoTransactionUPResult resposta = this.activationProcess.doTransaction(threadVO.getTransaction());

            if (Objects.nonNull(resposta)) {
                String responseCode = resposta.getResult();
                if (Objects.nonNull(responseCode)) {
                    switch (responseCode) {
                        case "0":
                            if (activation.getStatus() == ACTIVATION_STATUS.INCOMPLETE || activation.getStatus() == ACTIVATION_STATUS.REVERSAL) {
                                decisao = ACTIVATION_DECISION.APPROVED;
                            } else {
                                decisao = ACTIVATION_DECISION.INCOMPLETE;
                            }
                            break;
                        default:
                            decisao = ACTIVATION_DECISION.HARD;
                            break;
                    }
                }
            }

            log.info("DECISAO: " + decisao);

            if (Objects.nonNull(threadVO.getCallback())) {
                final EpayTransactionThreadEvent event = new EpayTransactionThreadEvent(threadVO.getId(), resposta, decisao, threadVO);
                threadVO.getCallback().stream().forEach(l -> l.notify(event));
            }
            threadVO = null;
        }
    }

    public void setActiveResource(Resource activeResource) {
        this.activeResource = activeResource;
    }

    public void setThreadVO(EpayTransactionThreadVO threadVO) {
        this.threadVO = threadVO;
    }
}
