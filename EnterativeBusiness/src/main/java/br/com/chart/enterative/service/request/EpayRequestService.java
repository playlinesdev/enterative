package br.com.chart.enterative.service.request;

import br.com.chart.enterative.dao.EpayTransactionDAO;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.EpayTransaction;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.epay.EpayDisplayCatalog;
import br.com.chart.enterative.vo.epay.EpayDoTransactionUPResult;
import br.com.chart.enterative.vo.epay.EpayRequestContainer;
import br.com.chart.enterative.xstream.converter.EpayLocalDateTimeXStreamConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.joox.JOOX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayRequestService extends UserAwareComponent {

    @Autowired
    private FluentService fluentService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private EpayTransactionDAO epayTransactionDAO;

    public EpayTransaction convert(EpayDoTransactionUPResult container) {
        EpayTransaction transaction = new EpayTransaction();
        transaction.setAmount(container.getAmount());
        transaction.setLocalDateTime(container.getLocalDateTime());
        transaction.setMessageNumber(container.getMessageNumber());
        transaction.setProductId(container.getProductId());
        transaction.setRequestType(container.getType());
        transaction.setResultCode(container.getResult());
        transaction.setResultText(container.getResultText());
        transaction.setServiceFee(container.getServiceFee());
        transaction.setTerminalId(container.getTerminalId());
        transaction.setTransactionId(container.getTransactionId());

        transaction.setHostTXID(container.getHostTXID());

        if (Objects.nonNull(container.getReceipt())) {
            if (Objects.nonNull(container.getReceipt().getCustomer()) && Objects.nonNull(container.getReceipt().getCustomer().getLines())) {
                transaction.setReceiptCustomer(container.getReceipt().getCustomer().getLines().stream().map(l -> l.getValue()).collect(Collectors.joining("\n")));
            }
            if (Objects.nonNull(container.getReceipt().getMerchant()) && Objects.nonNull(container.getReceipt().getMerchant().getLines())) {
                transaction.setReceiptMerchant(container.getReceipt().getMerchant().getLines().stream().map(l -> l.getValue()).collect(Collectors.joining("\n")));
            }
        }

        transaction.setServerDateTime(container.getServerDateTime());

        return transaction;
    }

    public EpayRequestContainer initLogin() {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("Login");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/AuthenticationService/Login");

        StringBuilder xml = new StringBuilder(0);
        String username = this.parameterDAO.get(ENVIRONMENT_PARAMETER.EPAY_USERNAME);
        String password = this.parameterDAO.get(ENVIRONMENT_PARAMETER.EPAY_PASSWORD);
        String appname = this.parameterDAO.get(ENVIRONMENT_PARAMETER.EPAY_APPNAME);
        xml.append(String.format("<v1:username>%s</v1:username>", username));
        xml.append(String.format("<v1:password>%s</v1:password>", password));
        xml.append(String.format("<v1:appName>%s</v1:appName>", appname));

        container.setXmlBody(xml.toString());
        return container;
    }

    public EpayTransaction initTransaction(EpayActivation activation) {
        EpayTransaction result = new EpayTransaction();

        result.setAmount(this.utils.toDotlessString(activation.getVoucher().getAmount()));
        result.setLocalDateTime(this.utils.formatEpayDate(activation.getCreatedAt()));
        result.setMessageNumber("0");
        result.setPhone(activation.getVoucher().getPhone());
        result.setProductId(activation.getVoucher().getEpayProductId());

        switch (activation.getStatus()) {
            case INCOMPLETE:
            case REVERSAL:
                List<EpayTransaction> transactions = this.epayTransactionDAO.findByActivationId(activation.getId());
                EpayTransaction saleTransaction = transactions.stream().filter(t -> Objects.equals(t.getRequestType(), "SALE") && 
                    t.getDirection() == TRANSACTION_DIRECTION.RETURN && Objects.equals(t.getResultCode(), "0")).findFirst().orElse(null);
                
                if (Objects.nonNull(saleTransaction)) {
                    result.setHostTXID(saleTransaction.getHostTXID());
                } else {
                    return null;
                }

                if (activation.getStatus() == ACTIVATION_STATUS.INCOMPLETE) {
                    result.setRequestType("ACK");
                } else {
                    result.setRequestType("Cancel");
                }
                break;
            default:
                result.setRequestType("SALE");
                break;
        }

        result.setSequence("0");
        result.setServiceFee("0");
        result.setTerminalId(activation.getTerminal());
        result.setTransactionId(activation.getTransactionId());

        return result;
    }

    public EpayRequestContainer initTransactionUp(String authId, Long terminalId, Long retailerAcc, EpayTransaction transaction) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("DoTransactionUP");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/UPService/DoTransactionUP");

        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));
        xml.append("<v1:clerkId />");
        xml.append("<v1:request>");
        xml.append(String.format("<v1:Amount>%s</v1:Amount>", transaction.getAmount()));
        xml.append("<v1:Authorization i:nil=\"true\" />");
        xml.append("<v1:Card i:nil=\"true\" />");
        xml.append("<v1:Cashier i:nil=\"true\" />");
        xml.append("<v1:Comment i:nil=\"true\" />");
        xml.append("<v1:Currency i:nil=\"true\" />");
        xml.append("<v1:CustomData i:nil=\"true\" />");
        xml.append("<v1:CustomerData i:nil=\"true\" />");
        xml.append("<v1:Device i:nil=\"true\" />");

        if (Objects.equals(transaction.getRequestType(), "SALE")) {
            xml.append("<v1:HostTransactionID i:nil=\"true\" />");
        } else {
            xml.append(String.format("<v1:HostTransactionID>%s</v1:HostTransactionID>", transaction.getHostTXID()));
        }

        xml.append(String.format("<v1:LocalDateTime>%s</v1:LocalDateTime>", this.utils.formatEpayDate(transaction.getCreatedAt())));
        xml.append(String.format("<v1:MessageNumber>%s</v1:MessageNumber>", transaction.getMessageNumber()));
        xml.append("<v1:Mode i:nil=\"true\" />");
        xml.append("<v1:POSEntry i:nil=\"true\" />");
        xml.append(String.format("<v1:Phone>%s</v1:Phone>", transaction.getPhone()));
        xml.append(String.format("<v1:ProductID>%s</v1:ProductID>", transaction.getProductId()));
        xml.append(String.format("<v1:RequestType>%s</v1:RequestType>", transaction.getRequestType()));
        xml.append("<v1:RetailerID i:nil=\"true\" />");
        xml.append(String.format("<v1:Sequence>%s</v1:Sequence>", transaction.getSequence()));
        xml.append(String.format("<v1:ServiceFee>%s</v1:ServiceFee>", transaction.getServiceFee()));
        xml.append("<v1:ServiceTypeId i:nil=\"true\" />");
        xml.append("<v1:ShopID i:nil=\"true\" />");
        xml.append("<v1:SubProductID i:nil=\"true\" />");
        xml.append("<v1:TerminalID>");
        xml.append(String.format("<a:RetailerAcc>%s</a:RetailerAcc>", retailerAcc));
        xml.append("<a:StoreId i:nil=\"true\" />");
        xml.append(String.format("<a:Value>%s</a:Value>", terminalId)); // transaction.getTerminalId()
        xml.append("</v1:TerminalID>");
        xml.append("<v1:TillID i:nil=\"true\" />");
        xml.append(String.format("<v1:TransactionID>%s</v1:TransactionID>", transaction.getTransactionId()));
        xml.append("<v1:TransactionReference i:nil=\"true\" />");

        xml.append("</v1:request>");

        container.setXmlBody(xml.toString());
        return container;
    }

    public EpayRequestContainer initDisplayCatalog(String authId) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("GetDisplayCatalog");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/CatalogService/GetDisplayCatalog");

        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));

        container.setXmlBody(xml.toString());
        return container;
    }

    public EpayRequestContainer initDisplayGroup(String authId, String category) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("GetDisplayGroup");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/CatalogService/GetDisplayGroup");

        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));
        xml.append(String.format("<v1:category>%s</v1:category>", category));

        container.setXmlBody(xml.toString());
        return container;
    }
    
    public EpayRequestContainer initGetSalesReport(String authId, Date start, Date end, Boolean detailed) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("GetSalesReport");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/AccountService/GetSalesReport");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));
        xml.append(String.format("<v1:InitialDate>%s</v1:InitialDate>", sdf.format(start)));
        xml.append(String.format("<v1:FinalDate>%s</v1:FinalDate>", sdf.format(end)));
        xml.append(String.format("<v1:isDetailed>%s</v1:isDetailed>", detailed));
        
        container.setXmlBody(xml.toString());
        return container;
    }
    
    public EpayRequestContainer initGetAttachment(String authId) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("GetAttachment");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/AccountService/GetAttachment");
        
        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));
        
        container.setXmlBody(xml.toString());
        return container;
    }

    public EpayRequestContainer initCatalogsByFilters(String authId, EpayDisplayCatalog displayCatalog, String areaCode) {
        EpayRequestContainer container = new EpayRequestContainer();
        container.setAction("GetCatalogsByFilters");
        container.setSoapAction("https://epaysvc.epay.com/Contract/V1.0/CatalogService/GetCatalogsByFilters");

        StringBuilder xml = new StringBuilder(0);
        xml.append(String.format("<v1:authId>%s</v1:authId>", authId));
        xml.append("<v1:filters>");
        xml.append("<f:Filter>");
        xml.append("<f:ConditionType>E</f:ConditionType>");
        xml.append("<f:Name>DisplayGroup</f:Name>");
        xml.append(String.format("<f:Value i:type=\"b:string\">%s</f:Value>", displayCatalog.getDisplayGroup()));
        xml.append("</f:Filter>");
        
        if (Objects.nonNull(areaCode) && !areaCode.isEmpty()) {
            xml.append("<f:Filter>");
            xml.append("<f:ConditionType>C</f:ConditionType>");
            xml.append("<f:Name>Extensions</f:Name>");
            xml.append(String.format("<f:Value i:type=\"b:string\">AREACODE;%s</f:Value>", areaCode));
            xml.append("</f:Filter>");
        }
        
        xml.append("</v1:filters>");

        container.setXmlBody(xml.toString());
        return container;
    }

    public String createSoapEnvelope(EpayRequestContainer container) {
        StringBuilder xml = new StringBuilder(0);
        xml.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"https://epaysvc.epay.com/Contract/V1.0\"");
        xml.append(" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:f=\"https://webservices.payspot.com/r09/v1/data/Filter\"");
        xml.append(" xmlns:b=\"http://www.w3.org/2001/XMLSchema\" xmlns:a=\"http://schemas.datacontract.org/2004/07/epay.Common.Messaging.UP\">");
        xml.append("<s:Body>");
        xml.append(String.format("<v1:%s>", container.getAction()));
        xml.append(container.getXmlBody());
        xml.append(String.format("</v1:%s>", container.getAction()));
        xml.append("</s:Body>");
        xml.append("</s:Envelope>");
        return xml.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T send(EpayRequestContainer request, String endpoint, Class<T> responseClass) {
        T response = null;
        try {
            String soapEnvelope = this.createSoapEnvelope(request);
            this.log("[EpayRequestService] SOAP Envelope: " + soapEnvelope);

            String xmlResponse = this.fluentService.sendEpay(endpoint, soapEnvelope, request.getSoapAction());
            this.log("[EpayRequestService] XML Response: " + xmlResponse);

            if (Objects.nonNull(xmlResponse) && !xmlResponse.isEmpty()) {
                XStream xStream = new XStream(new DomDriver());
                xStream.setClassLoader(Thread.currentThread().getContextClassLoader());
                xStream.registerConverter(new EpayLocalDateTimeXStreamConverter());
                xStream.processAnnotations(responseClass);
                xStream.ignoreUnknownElements();

                String xmlResponseBody = JOOX.$(xmlResponse).namespace("s", "http://schemas.xmlsoap.org/soap/envelope/").xpath("//s:Body").content();
                response = (T) xStream.fromXML(xmlResponseBody);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.log("[EpayRequestService] Timeout");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
