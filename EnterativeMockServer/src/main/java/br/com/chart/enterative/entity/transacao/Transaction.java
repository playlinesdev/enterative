package br.com.chart.enterative.entity.transacao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {

    private String acquiringInstitutionIdentifier;
    private String localTransactionDate;
    private String localTransactionTime;
    private String merchantCategoryCode;
    private String merchantIdentifier;
    private String merchantLocation;
    private String merchantTerminalId;
    private String pointOfServiceEntryMode;
    private String primaryAccountNumber;
    private String processingCode;
    private String retrievalReferenceNumber;
    private String systemTraceAuditNumber;
    private String transactionAmount;
    private String transactionCurrencyCode;
    private String transmissionDateTime;
    private String authIdentificationResponse;
    private String responseCode;
    private String networkManagementCode;
    private String termsAndConditions;
    private AdditionalTxnFields additionalTxnFields;
    private ReceiptsFields receiptsFields;

    public Transaction() {
        this.additionalTxnFields = new AdditionalTxnFields();
        this.receiptsFields = new ReceiptsFields();
    }

    public String getAcquiringInstitutionIdentifier() {
        return acquiringInstitutionIdentifier;
    }

    public void setAcquiringInstitutionIdentifier(String acquiringInstitutionIdentifier) {
        this.acquiringInstitutionIdentifier = acquiringInstitutionIdentifier;
    }

    public AdditionalTxnFields getAdditionalTxnFields() {
        return additionalTxnFields;
    }

    public void setAdditionalTxnFields(AdditionalTxnFields additionalTxnFields) {
        this.additionalTxnFields = additionalTxnFields;
    }

    public String getLocalTransactionDate() {
        return localTransactionDate;
    }

    public void setLocalTransactionDate(String localTransactionDate) {
        this.localTransactionDate = localTransactionDate;
    }

    public String getLocalTransactionTime() {
        return localTransactionTime;
    }

    public void setLocalTransactionTime(String localTransactionTime) {
        this.localTransactionTime = localTransactionTime;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getMerchantIdentifier() {
        return merchantIdentifier;
    }

    public void setMerchantIdentifier(String merchantIdentifier) {
        this.merchantIdentifier = merchantIdentifier;
    }

    public String getMerchantLocation() {
        return merchantLocation;
    }

    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }

    public String getMerchantTerminalId() {
        return merchantTerminalId;
    }

    public void setMerchantTerminalId(String merchantTerminalId) {
        this.merchantTerminalId = merchantTerminalId;
    }

    public String getPointOfServiceEntryMode() {
        return pointOfServiceEntryMode;
    }

    public void setPointOfServiceEntryMode(String pointOfServiceEntryMode) {
        this.pointOfServiceEntryMode = pointOfServiceEntryMode;
    }

    public String getPrimaryAccountNumber() {
        return primaryAccountNumber;
    }

    public void setPrimaryAccountNumber(String primaryAccountNumber) {
        this.primaryAccountNumber = primaryAccountNumber;
    }

    public String getProcessingCode() {
        return processingCode;
    }

    public void setProcessingCode(String processingCode) {
        this.processingCode = processingCode;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getSystemTraceAuditNumber() {
        return systemTraceAuditNumber;
    }

    public void setSystemTraceAuditNumber(String systemTraceAuditNumber) {
        this.systemTraceAuditNumber = systemTraceAuditNumber;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionCurrencyCode() {
        return transactionCurrencyCode;
    }

    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        this.transactionCurrencyCode = transactionCurrencyCode;
    }

    public String getTransmissionDateTime() {
        return transmissionDateTime;
    }

    public void setTransmissionDateTime(String transmissionDateTime) {
        this.transmissionDateTime = transmissionDateTime;
    }

    public String getAuthIdentificationResponse() {
        return authIdentificationResponse;
    }

    public void setAuthIdentificationResponse(String authIdentificationResponse) {
        this.authIdentificationResponse = authIdentificationResponse;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getNetworkManagementCode() {
        return networkManagementCode;
    }

    public void setNetworkManagementCode(String networkManagementCode) {
        this.networkManagementCode = networkManagementCode;
    }

    public ReceiptsFields getReceiptsFields() {
        return receiptsFields;
    }

    public void setReceiptsFields(ReceiptsFields receiptsFields) {
        this.receiptsFields = receiptsFields;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }
}
