package br.com.chart.enterative.service.activationprocess;

import br.com.chart.enterative.converter.WSRequestConverterService;
import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.BHNVoucherDAO;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.dao.ResourceDAO;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.ActivationProcessService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.request.BHNRequestService;
import br.com.chart.enterative.vo.ActiveResourceVO;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLine;
import br.com.chart.enterative.vo.bhn.BHNRequestContainer;
import br.com.chart.enterative.vo.bhn.BHNResponseContainer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNActivationProcess extends ActivationProcessService {

    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private BHNRequestService requestService;

    @Autowired
    private EnvParameterDAO parameterDAO;

    @Autowired
    private BHNActivationDAO activationDAO;

    @Autowired
    private BHNVoucherDAO voucherDAO;

    @Autowired
    private WSRequestConverterService wsRequestConverterService;
    
    @Autowired
    private SaleOrderLineDAO saleOrderLineDAO;
    
    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;
    
    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Server __executeEcho(List<Server> activeServers) {
        Server activeServer = null;

        for (Server server : activeServers) {
            if (Objects.nonNull(activeServer)) {
                break;
            }

            List<Resource> serverResources = this.resourceDAO.findByServer(server);
            if (Objects.nonNull(serverResources) && !serverResources.isEmpty()) {
                serverResources = serverResources.stream().filter(r -> r.getType() == RESOURCE_TYPE.NETWORK).collect(Collectors.toList());
                for (Resource resource : serverResources) {
                    BHNRequestContainer request = this.requestService.initECHO(server.getEchoProduct());
                    try {
                        this.log("ECHO [%s] :: Sending to [%s]", this.process(), resource.getUrl());
                        String endpoint = resource.getUrl();

                        BHNResponseContainer resposta = this.requestService.send(request, endpoint);

                        String responseCode = resposta.getResponse().getTransaction().getResponseCode();
                        if (null != resposta && Objects.equals(responseCode, "00")) {
                            activeServer = resource.getServer();
                            break;
                        } else {
                            this.log("ECHO [%s] :: Response Code [%s]", this.process(), responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.log("ECHO [%s] :: Error [%s]", this.process(), e.getMessage());
                    }
                }
            } else {
                this.log("ECHO [%s] :: Server [%s] without resources", this.process(), server.getName());
            }
        }

        return activeServer;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public Server executeEcho(List<Server> activeServers) {
        Server activeServer = this.__executeEcho(activeServers);

        if (Objects.isNull(activeServer)) {
            this.log("ECHO [%s] :: Initial attempt failed");
            int maxRetries = this.parameterDAO.get(ENVIRONMENT_PARAMETER.ECHO_MAX_RETRIES);
            int retryCount = 1;

            while (Objects.isNull(activeServer) && retryCount < maxRetries) {
                try {
                    this.log("ECHO [%s] :: Next attempt in 10 seconds");
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException ex) {
                }
                this.log("ECHO [%s] :: Attempt [%s/%s]", this.process(), (retryCount + 1), maxRetries);
                activeServer = this.__executeEcho(activeServers);
                retryCount++;
            }
        }

        return activeServer;
    }

    @Override
    public boolean validateServer(Server server) {
        List<Resource> resources = this.resourceDAO.findByServer(server);
        boolean existeRecursoFinancial = resources.stream().anyMatch(r -> r.getType() == RESOURCE_TYPE.FINANCIAL);
        boolean existeRecursoEcho = resources.stream().anyMatch(r -> r.getType() == RESOURCE_TYPE.NETWORK);
        boolean existeRecursoCancelamento = resources.stream().anyMatch(r -> r.getType() == RESOURCE_TYPE.REVERSAL);

        return existeRecursoFinancial && existeRecursoEcho && existeRecursoCancelamento;
    }

    @Override
    public ActiveResourceVO retrieveResource(Server server) {
        List<Resource> resources = this.resourceDAO.findByServer(server);
        Optional<Resource> resource = resources.stream().filter(r -> r.getType() == RESOURCE_TYPE.FINANCIAL).findFirst();

        ActiveResourceVO activeResource = new ActiveResourceVO();
        activeResource.setExpirationDate(null);
        activeResource.setResource(resource.orElse(null));
        return activeResource;
    }

    @Override
    public String process() {
        return ACTIVATION_PROCESS.BHN.getDescription();
    }

    public void activate(WSRequest request, WSRequestLine l, Merchant merchant, Long systemUserID) {
        BHNActivation activation = this.activationDAO.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, request.getShop(), request.getTerminal(), l.getExternalCode());

        if (Objects.nonNull(activation)) {
            //result = this.__verifyStatus(ativacao);
            RESPONSE_CODE code = RESPONSE_CODE.E16;
            l.setActivationStatus(null);
            l.setResponseAux(null);
            l.setResponse(code);
        } else {
            activation = this.wsRequestConverterService.toBHNActivation(request, l.getExternalCode());
            BHNVoucher voucher = this.wsRequestConverterService.toBHNVoucher(l.getBarcode());
            activation.setVoucher(voucher);

            this.voucherDAO.saveAndFlush(voucher, systemUserID);
            this.activationDAO.saveAndFlush(activation, systemUserID);

            RESPONSE_CODE code = RESPONSE_CODE.E00;
            l.setActivationStatus(activation.getStatus());
            l.setResponse(code);
            l.setResponseAux(null);
        }
    }
}
