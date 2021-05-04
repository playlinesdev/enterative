package br.com.chart.enterative.service.request;

import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Supplier;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.vo.SequencialVO;
import br.com.chart.enterative.vo.bhn.BHNRequestAdditionalTxnFields;
import br.com.chart.enterative.vo.bhn.BHNRequestContainer;
import br.com.chart.enterative.vo.bhn.BHNRequestDetails;
import br.com.chart.enterative.vo.bhn.BHNRequestHeader;
import br.com.chart.enterative.vo.bhn.BHNRequestReceiptsFields;
import br.com.chart.enterative.vo.bhn.BHNRequestTransaction;
import br.com.chart.enterative.vo.bhn.BHNResponseContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNRequestService extends UserAwareComponent {

    @Autowired
    private EnvParameterCRUDService parameterCRUDService;

    @Autowired
    private HelperConverterService helperConverterService;

    @Autowired
    private FluentService fluentService;

    private static final String PHYSICAL_POINT_OF_SERVICE_ENTRY_MODE = "031";
    private static final String PHYSICAL_PROCESSING_CODE = "725400";
    private static final String VIRTUAL_POINT_OF_SERVICE_ENTRY_MODE = "041";
    private static final String VIRTUAL_PROCESSING_CODE = "745400";
    private static final String TRANSACTION_CURRENCY_CODE = "986";
    private static final String PRODUCT_CATEGORY_CODE = "01";
    //Datas
    private static String localTransactionDate;
    private static String localTransactionTime;
    private static String transmissionDateTime;

    public BHNResponseContainer send(BHNRequestContainer transacao, String endpoint) {
        ObjectMapper objectMapper = new ObjectMapper();
        BHNResponseContainer retorno = null;
        try {
            // Necessario para não enviar no json o array em branco.
            transacao.getTransaction().setReceiptsFields(null);

            String jsonInString = objectMapper.writeValueAsString(transacao);

            // Leitura de Variavel de ambiente para gerar artefatos para BHN
            // String exibirJson = System.getenv("WSENTERATIVE_EXIBIR_JSON");
            String exibirJson = "SIM";
            this.log("exibirJson:" + exibirJson);
            if ("SIM".equalsIgnoreCase(exibirJson)) {
                this.log("Envio: " + jsonInString);
            }

            String retornoSTR = this.fluentService.sendBHN(endpoint, jsonInString);

            if ("SIM".equalsIgnoreCase(exibirJson)) {
                this.log("Retorno: " + retornoSTR);
            }

            if (Objects.nonNull(retornoSTR) && !retornoSTR.isEmpty()) {
                retorno = this.readFromJson(retornoSTR);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.log("Identificado um Time-out");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }

    private void initTransactionDateTime() {
        Date dataAtual = new Date();
        //SimpleDateFormat sdfData = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat sdfData = new SimpleDateFormat("yyMMdd");
        SimpleDateFormat sdfHora = new SimpleDateFormat("HHmmss");
        //SimpleDateFormat sdfUTC = new SimpleDateFormat("ddMMyyHHmmss");
        SimpleDateFormat sdfUTC = new SimpleDateFormat("yyMMddHHmmss");
        sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        localTransactionDate = sdfData.format(dataAtual);
        localTransactionTime = sdfHora.format(dataAtual);
        transmissionDateTime = sdfUTC.format(dataAtual);
    }

    public BHNRequestContainer initECHO(Product product) {
        this.initTransactionDateTime();

        Supplier supplier = product.getSupplier();

        if (Objects.isNull(supplier)) {
            throw new RuntimeException("Produto vinculado ao servidor não possui fornecedor");
        }

        BHNRequestContainer requestContainer = new BHNRequestContainer();

        BHNRequestDetails details = new BHNRequestDetails();
        details.setProductCategoryCode(PRODUCT_CATEGORY_CODE);
        details.setSpecVersion(supplier.getSpecVersion());

        BHNRequestHeader header = new BHNRequestHeader();
        header.setDetails(details);
        header.setSignature(supplier.getSignature());

        BHNRequestTransaction transaction = new BHNRequestTransaction();
        transaction.setTransmissionDateTime(transmissionDateTime);
        transaction.setNetworkManagementCode("301");
        transaction.setSystemTraceAuditNumber(this.parameterCRUDService.getSequenciais(this.systemUserId()).getSystemTraceAuditNumber());

        requestContainer.setHeader(header);
        requestContainer.setTransaction(transaction);

        return requestContainer;
    }

    public BHNRequestContainer initReversal(BHNTransaction originalTransaction) {

        this.initTransactionDateTime();

        BHNRequestContainer container = new BHNRequestContainer();

        BHNRequestDetails details = new BHNRequestDetails();
        details.setProductCategoryCode(PRODUCT_CATEGORY_CODE);
        details.setSpecVersion(originalTransaction.getSpecVersion());

        BHNRequestHeader header = new BHNRequestHeader();
        header.setDetails(details);
        header.setSignature(originalTransaction.getSignature());

        BHNRequestAdditionalTxnFields additionalTxnFields = new BHNRequestAdditionalTxnFields();
        additionalTxnFields.setProductId(originalTransaction.getProductId());

        BHNRequestTransaction transaction = new BHNRequestTransaction();
        transaction.setAcquiringInstitutionIdentifier(originalTransaction.getAcquiringInstitutionIdentifier());
        transaction.setAdditionalTxnFields(additionalTxnFields);

        transaction.setLocalTransactionDate(originalTransaction.getLocalTransactionDate());
        transaction.setLocalTransactionTime(originalTransaction.getLocalTransactionTime());
        transaction.setTransmissionDateTime(transmissionDateTime);

        transaction.setMerchantCategoryCode(originalTransaction.getMerchantCategoryCode());
        transaction.setMerchantIdentifier(originalTransaction.getMerchantIdentifier());
        transaction.setMerchantLocation(originalTransaction.getMerchantLocation());
        transaction.setMerchantTerminalId(originalTransaction.getMerchantTerminalId());
        transaction.setPrimaryAccountNumber(originalTransaction.getPrimaryAccountNumber());

        ACTIVATION_TYPE activationType;
        if (Objects.isNull(originalTransaction.getBhnActivation().getType())) {
            activationType = ACTIVATION_TYPE.PHYSICAL;
        } else {
            activationType = originalTransaction.getBhnActivation().getType();
        }

        if (activationType.equals(ACTIVATION_TYPE.PHYSICAL)) {
            transaction.setProcessingCode(PHYSICAL_PROCESSING_CODE);
            transaction.setPointOfServiceEntryMode(PHYSICAL_POINT_OF_SERVICE_ENTRY_MODE);

        } else {
            transaction.setProcessingCode(VIRTUAL_PROCESSING_CODE);
            transaction.setPointOfServiceEntryMode(VIRTUAL_POINT_OF_SERVICE_ENTRY_MODE);
        }

        //Preenche sequenciais de uma instancia Singleton
        SequencialVO sequenciais = this.parameterCRUDService.getSequenciais(this.systemUserId());
        transaction.setRetrievalReferenceNumber(originalTransaction.getRetrievalReferenceNumber());
        transaction.setSystemTraceAuditNumber(sequenciais.getSystemTraceAuditNumber());

        //
        transaction.setTransactionAmount(originalTransaction.getTransactionAmount());

        transaction.setTransactionCurrencyCode(TRANSACTION_CURRENCY_CODE);

        container.setHeader(header);
        container.setTransaction(transaction);

        return container;
    }

    public BHNRequestContainer initRequest(String cardNo, String upc, String amount, String shopCode, String terminalCode, ACTIVATION_TYPE tipoAtivacao, Merchant merchant, Supplier supplier, String primaryAccountNumber) {

        //Inicializa os campos de data e hora
        this.initTransactionDateTime();

        BHNRequestContainer container = new BHNRequestContainer();

        BHNRequestDetails details = new BHNRequestDetails();
        details.setProductCategoryCode(PRODUCT_CATEGORY_CODE);
        details.setSpecVersion(supplier.getSpecVersion());

        BHNRequestHeader header = new BHNRequestHeader();
        header.setDetails(details);
        header.setSignature(supplier.getSignature());

        BHNRequestAdditionalTxnFields additionalTxnFields = new BHNRequestAdditionalTxnFields();
        additionalTxnFields.setProductId(upc);

        BHNRequestTransaction transaction = new BHNRequestTransaction();
        transaction.setAcquiringInstitutionIdentifier(merchant.getAcquiringInstitutionIdentifier());
        transaction.setAdditionalTxnFields(additionalTxnFields);

        transaction.setLocalTransactionDate(localTransactionDate);
        transaction.setLocalTransactionTime(localTransactionTime);
        transaction.setTransmissionDateTime(transmissionDateTime);

        transaction.setMerchantCategoryCode(merchant.getCategory().getCode());
        transaction.setMerchantIdentifier(merchant.getMerchantIdentifier());
        transaction.setMerchantLocation(merchant.getMerchantLocation());
        transaction.setMerchantTerminalId(this.helperConverterService.retrieveMerchantTerminalID(shopCode, terminalCode));

        if (tipoAtivacao.equals(ACTIVATION_TYPE.PHYSICAL)) {
            transaction.setProcessingCode(PHYSICAL_PROCESSING_CODE);
            transaction.setPointOfServiceEntryMode(PHYSICAL_POINT_OF_SERVICE_ENTRY_MODE);
            transaction.setPrimaryAccountNumber(cardNo);
        } else {
            transaction.setProcessingCode(VIRTUAL_PROCESSING_CODE);
            transaction.setPointOfServiceEntryMode(VIRTUAL_POINT_OF_SERVICE_ENTRY_MODE);
            transaction.setPrimaryAccountNumber(primaryAccountNumber);
        }

        //Preenche sequenciais de uma instancia Singleton
        SequencialVO sequenciais = this.parameterCRUDService.getSequenciais(this.systemUserId());
        transaction.setRetrievalReferenceNumber(sequenciais.getRetrievalReferenceNumber());
        transaction.setSystemTraceAuditNumber(sequenciais.getSystemTraceAuditNumber());

        //
        if (Objects.nonNull(amount)) {
            transaction.setTransactionAmount(amount);
        } else {
            transaction.setTransactionAmount("000000002500");
        }
        transaction.setTransactionCurrencyCode(TRANSACTION_CURRENCY_CODE);

        container.setHeader(header);
        container.setTransaction(transaction);

        return container;
    }

    public BHNTransaction convert(BHNRequestContainer container) {
        BHNTransaction transaction = new BHNTransaction();

        if (Objects.nonNull(container.getHeader())) {
            if (Objects.nonNull(container.getHeader().getDetails())) {
                transaction.setProductCategoryCode(container.getHeader().getDetails().getProductCategoryCode());
                transaction.setSpecVersion(container.getHeader().getDetails().getSpecVersion());
                transaction.setStatusCode(container.getHeader().getDetails().getStatusCode());
            }
            transaction.setSignature(container.getHeader().getSignature());
        }
        if (Objects.nonNull(container.getTransaction())) {
            transaction.setAcquiringInstitutionIdentifier(container.getTransaction().getAcquiringInstitutionIdentifier());
            if (Objects.nonNull(container.getTransaction().getAdditionalTxnFields())) {
                transaction.setActivationAccountNumber(container.getTransaction().getAdditionalTxnFields().getActivationAccountNumber());
                transaction.setBalanceAmount(container.getTransaction().getAdditionalTxnFields().getBalanceAmount());
                transaction.setCorrelatedTransactionUniqueId(container.getTransaction().getAdditionalTxnFields().getCorrelatedTransactionUniqueId());
                transaction.setProductId(container.getTransaction().getAdditionalTxnFields().getProductId());
                transaction.setRedemptionAccountNumber(container.getTransaction().getAdditionalTxnFields().getRedemptionAccountNumber());
                transaction.setRedemptionPin(container.getTransaction().getAdditionalTxnFields().getRedemptionPin());
                transaction.setTransactionUniqueId(container.getTransaction().getAdditionalTxnFields().getTransactionUniqueId());
            }
            transaction.setAuthIdentificationResponse(container.getTransaction().getAuthIdentificationResponse());
            transaction.setLocalTransactionDate(container.getTransaction().getLocalTransactionDate());
            transaction.setLocalTransactionTime(container.getTransaction().getLocalTransactionTime());
            transaction.setMerchantCategoryCode(container.getTransaction().getMerchantCategoryCode());
            transaction.setMerchantIdentifier(container.getTransaction().getMerchantIdentifier());
            transaction.setMerchantLocation(container.getTransaction().getMerchantLocation());
            transaction.setMerchantTerminalId(container.getTransaction().getMerchantTerminalId());
            transaction.setNetworkManagementCode(container.getTransaction().getNetworkManagementCode());
            transaction.setPointOfServiceEntryMode(container.getTransaction().getPointOfServiceEntryMode());
            transaction.setPrimaryAccountNumber(container.getTransaction().getPrimaryAccountNumber());
            transaction.setProcessingCode(container.getTransaction().getProcessingCode());
            
            if (Objects.nonNull(container.getTransaction().getReceiptsFields()) && Objects.nonNull(container.getTransaction().getReceiptsFields().getLine())) {
                transaction.setLine(Arrays.stream(container.getTransaction().getReceiptsFields().getLine()).collect(Collectors.joining("\n")));
            } else {
                transaction.setLine("");
            }
            
            transaction.setResponseCode(container.getTransaction().getResponseCode());
            transaction.setRetrievalReferenceNumber(container.getTransaction().getRetrievalReferenceNumber());
            transaction.setSystemTraceAuditNumber(container.getTransaction().getSystemTraceAuditNumber());
            transaction.setTermsAndConditions(container.getTransaction().getTermsAndConditions());
            transaction.setTransactionAmount(container.getTransaction().getTransactionAmount());
            transaction.setTransactionCurrencyCode(container.getTransaction().getTransactionCurrencyCode());
            transaction.setTransmissionDateTime(container.getTransaction().getTransmissionDateTime());
        }

        return transaction;
    }

    public BHNRequestContainer convert(BHNTransaction transaction) {
        BHNRequestDetails details = new BHNRequestDetails();
        details.setProductCategoryCode(transaction.getProductCategoryCode());
        details.setSpecVersion(transaction.getSpecVersion());
        details.setStatusCode(transaction.getStatusCode());

        BHNRequestHeader header = new BHNRequestHeader();
        header.setDetails(details);
        header.setSignature(transaction.getSignature());

        BHNRequestAdditionalTxnFields textFields = new BHNRequestAdditionalTxnFields();
        textFields.setActivationAccountNumber(transaction.getActivationAccountNumber());
        textFields.setBalanceAmount(transaction.getBalanceAmount());
        textFields.setCorrelatedTransactionUniqueId(transaction.getCorrelatedTransactionUniqueId());
        textFields.setProductId(transaction.getProductId());
        textFields.setRedemptionAccountNumber(transaction.getRedemptionAccountNumber());
        textFields.setRedemptionPin(transaction.getRedemptionPin());
        textFields.setTransactionUniqueId(transaction.getTransactionUniqueId());

        BHNRequestReceiptsFields receiptFields = new BHNRequestReceiptsFields();
        if (Objects.nonNull(transaction.getLine())) {
            receiptFields.setLine(transaction.getLine().split("\n"));
        } else {
            receiptFields.setLine(new String[0]);
        }

        BHNRequestTransaction bhnTransaction = new BHNRequestTransaction();
        bhnTransaction.setAcquiringInstitutionIdentifier(transaction.getAcquiringInstitutionIdentifier());
        bhnTransaction.setAdditionalTxnFields(textFields);
        bhnTransaction.setAuthIdentificationResponse(transaction.getAuthIdentificationResponse());
        bhnTransaction.setLocalTransactionDate(transaction.getLocalTransactionDate());
        bhnTransaction.setLocalTransactionTime(transaction.getLocalTransactionTime());
        bhnTransaction.setMerchantCategoryCode(transaction.getMerchantCategoryCode());
        bhnTransaction.setMerchantIdentifier(transaction.getMerchantIdentifier());
        bhnTransaction.setMerchantLocation(transaction.getMerchantLocation());
        bhnTransaction.setMerchantTerminalId(transaction.getMerchantTerminalId());
        bhnTransaction.setNetworkManagementCode(transaction.getNetworkManagementCode());
        bhnTransaction.setPointOfServiceEntryMode(transaction.getPointOfServiceEntryMode());
        bhnTransaction.setPrimaryAccountNumber(transaction.getPrimaryAccountNumber());
        bhnTransaction.setProcessingCode(transaction.getProcessingCode());
        bhnTransaction.setReceiptsFields(receiptFields);
        bhnTransaction.setResponseCode(transaction.getResponseCode());
        bhnTransaction.setRetrievalReferenceNumber(transaction.getRetrievalReferenceNumber());
        bhnTransaction.setSystemTraceAuditNumber(transaction.getSystemTraceAuditNumber());
        bhnTransaction.setTermsAndConditions(transaction.getTermsAndConditions());
        bhnTransaction.setTransactionAmount(transaction.getTransactionAmount());
        bhnTransaction.setTransactionCurrencyCode(transaction.getTransactionCurrencyCode());
        bhnTransaction.setTransmissionDateTime(transaction.getTransmissionDateTime());

        BHNRequestContainer container = new BHNRequestContainer();
        container.setHeader(header);
        container.setTransaction(bhnTransaction);

        return container;
    }

    // Abandon all hope ye who enters here
    public BHNResponseContainer readFromJson(String json) throws IOException {
        JSONObject jsonObject = JSONObject.fromObject(json);
        System.out.println("net.sf.json.JSONObject: " + jsonObject);

        ObjectMapper objMapper = new ObjectMapper();

        JsonNode jsonNode = objMapper.readTree(jsonObject.toString());
        BHNResponseContainer result = objMapper.treeToValue(jsonNode, BHNResponseContainer.class);
        return result;
    }
}
