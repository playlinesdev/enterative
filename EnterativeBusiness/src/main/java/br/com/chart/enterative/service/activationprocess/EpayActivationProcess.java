package br.com.chart.enterative.service.activationprocess;

import br.com.chart.enterative.converter.WSRequestConverterService;
import br.com.chart.enterative.dao.EpayActivationDAO;
import br.com.chart.enterative.dao.EpayVoucherDAO;
import br.com.chart.enterative.dao.ResourceDAO;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.EpayTransaction;
import br.com.chart.enterative.entity.EpayVoucher;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.ECHOService;
import br.com.chart.enterative.service.base.ActivationProcessService;
import br.com.chart.enterative.service.request.EpayRequestService;
import br.com.chart.enterative.vo.ActiveResourceVO;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLine;
import br.com.chart.enterative.vo.epay.EpayCatalog;
import br.com.chart.enterative.vo.epay.EpayDisplayCatalog;
import br.com.chart.enterative.vo.epay.EpayDoTransactionUPResponse;
import br.com.chart.enterative.vo.epay.EpayDoTransactionUPResult;
import br.com.chart.enterative.vo.epay.EpayGetAttachmentResponse;
import br.com.chart.enterative.vo.epay.EpayGetCatalogsByFiltersResponse;
import br.com.chart.enterative.vo.epay.EpayGetDisplayCatalogResponse;
import br.com.chart.enterative.vo.epay.EpayGetDisplayGroupResponse;
import br.com.chart.enterative.vo.epay.EpayGetSalesReportResponse;
import br.com.chart.enterative.vo.epay.EpayLoginResponse;
import br.com.chart.enterative.vo.epay.EpayReport;
import br.com.chart.enterative.vo.epay.EpayRequestContainer;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayActivationProcess extends ActivationProcessService {

    @Autowired
    private ResourceDAO resourceDAO;
    
    @Autowired
    private EpayRequestService requestService;
    
    @Autowired
    private ECHOService echoService;
    
    @Autowired
    private EpayActivationDAO activationDAO;
    
    @Autowired
    private EpayVoucherDAO voucherDAO;
    
    @Autowired
    private WSRequestConverterService wsRequestConverterService;

    @Override
    public Server executeEcho(List<Server> activeServers) {
        return activeServers.stream().findFirst().orElse(null);
    }

    @Override
    public String process() {
        return ACTIVATION_PROCESS.EPAY.getDescription();
    }

    @Override
    public boolean validateServer(Server server) {
        List<Resource> resources = this.resourceDAO.findByServer(server);
        boolean existeRecursoFinancial = resources.stream().anyMatch(r -> r.getType() == RESOURCE_TYPE.FINANCIAL);
        boolean existeRecursoEcho = resources.stream().anyMatch(r -> r.getType() == RESOURCE_TYPE.NETWORK);

        return existeRecursoFinancial && existeRecursoEcho;
    }
    
    public EpayCatalog[] retrieveCatalogs(String areaCode, EpayDisplayCatalog displayCatalog) {
        EpayCatalog[] result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initCatalogsByFilters(activeResource.getAuthId(), displayCatalog, areaCode);
            try {
                EpayGetCatalogsByFiltersResponse response = this.requestService.send(request, activeResource.getResource().getUrl(), EpayGetCatalogsByFiltersResponse.class);
                result = response.getResult().getCatalogs().toArray(new EpayCatalog[response.getResult().getCatalogs().size()]);
            } catch (Exception e) {
                e.printStackTrace();
            }    
        }
        
        return result;
    }
    
    public EpayDoTransactionUPResult doTransaction(EpayTransaction transaction) {
        EpayDoTransactionUPResult result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initTransactionUp(activeResource.getAuthId(), activeResource.getTerminalId(), activeResource.getRetailerAcc(), transaction);
            try {
                EpayDoTransactionUPResponse response = this.requestService.send(request, activeResource.getResource().getUrl(), EpayDoTransactionUPResponse.class);
                result = response.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public EpayReport retrieveSalesReport(Date start, Date end) {
        EpayReport result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initGetSalesReport(activeResource.getAuthId(), start, end, true);
            try {
                EpayGetSalesReportResponse response = this.requestService.send(request, activeResource.getResource().getUrl(), EpayGetSalesReportResponse.class);
                result = response.getResult().getReport();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public EpayGetAttachmentResponse retrieveAttachment() {
        EpayGetAttachmentResponse result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initGetAttachment(activeResource.getAuthId());
            try {
                result = this.requestService.send(request, activeResource.getResource().getUrl(), EpayGetAttachmentResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public List<EpayDisplayCatalog> retrieveDisplayCatalogs() {
        List<EpayDisplayCatalog> result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initDisplayCatalog(activeResource.getAuthId());
            try {
                EpayGetDisplayCatalogResponse response = this.requestService.send(request, activeResource.getResource().getUrl(), EpayGetDisplayCatalogResponse.class);
                result = response.getDisplayResult().getCatalogs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return result;
    }
    
    public String[] retrieveDisplayGroups() {
        String[] result = null;
        ActiveResourceVO activeResource = this.echoService.retrieveActiveResource(ACTIVATION_PROCESS.EPAY);
        
        if (Objects.nonNull(activeResource)) {
            EpayRequestContainer request = this.requestService.initDisplayGroup(activeResource.getAuthId(), "By Operator");
            try {
                EpayGetDisplayGroupResponse response = this.requestService.send(request, activeResource.getResource().getUrl(), EpayGetDisplayGroupResponse.class);
                result = response.getResult().getDisplayGroups();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public ActiveResourceVO retrieveResource(Server server) {
        ActiveResourceVO activeResource = new ActiveResourceVO();
        
        List<Resource> serverResources = this.resourceDAO.findByServer(server);
        if (Objects.nonNull(serverResources) && !serverResources.isEmpty()) {
            List<Resource> networkResources = serverResources.stream().filter(r -> r.getType() == RESOURCE_TYPE.NETWORK).collect(Collectors.toList());
            for (Resource resource : networkResources) {
                EpayRequestContainer request = this.requestService.initLogin();
                try {
                    this.log("retrieveResource [%s] :: Sending to [%s]", this.process(), resource.getUrl());
                    String endpoint = resource.getUrl();

                    EpayLoginResponse response = this.requestService.send(request, endpoint, EpayLoginResponse.class);
                    activeResource.setExpirationDate(response.getLoginResult().getExpiration());
                    activeResource.setAuthId(response.getLoginResult().getAuthenticationId());
                    activeResource.setResource(serverResources.stream().filter(r -> r.getType() == RESOURCE_TYPE.FINANCIAL).findFirst().orElse(null));
                    activeResource.setRetailerAcc(response.getLoginResult().getAppProfile().getRetailerAcc());
                    activeResource.setTerminalId(response.getLoginResult().getAppProfile().getTerminalId());
                } catch (Exception e) {
                    e.printStackTrace();
                    this.log("retrieveResource [%s] :: Error [%s]", this.process(), e.getMessage());
                }
            }
        } else {
            this.log("retrieveResource [%s] :: Server [%s] without resources", this.process(), server.getName());
        }
        
        return activeResource;
    }

    @Override
    public void activate(WSRequest request, WSRequestLine l, Merchant merchant, Long systemUserID) {
        EpayActivation activation = this.activationDAO.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, request.getShop(), request.getTerminal(), l.getExternalCode());
        
        if (Objects.nonNull(activation)) {
            RESPONSE_CODE code = RESPONSE_CODE.E16;
            l.setActivationStatus(null);
            l.setResponseAux(null);
            l.setResponse(code);
        } else {
            activation = this.wsRequestConverterService.toEpayActivation(request, l.getExternalCode());
            
            EpayVoucher voucher = this.wsRequestConverterService.toEpayVoucher(l.getBarcode(), l.getEpay());
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
