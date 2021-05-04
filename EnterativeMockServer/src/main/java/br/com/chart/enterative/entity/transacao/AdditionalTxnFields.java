package br.com.chart.enterative.entity.transacao;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdditionalTxnFields {

    private String productId;
    private String balanceAmount;
    private String redemptionPin;
    private String redemptionAccountNumber;
    private String activationAccountNumber;
    private String transactionUniqueId;
    private String correlatedTransactionUniqueId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getRedemptionPin() {
        return redemptionPin;
    }

    public void setRedemptionPin(String redemptionPin) {
        this.redemptionPin = redemptionPin;
    }

    public String getRedemptionAccountNumber() {
        return redemptionAccountNumber;
    }

    public void setRedemptionAccountNumber(String redemptionAccountNumber) {
        this.redemptionAccountNumber = redemptionAccountNumber;
    }

    public String getActivationAccountNumber() {
        return activationAccountNumber;
    }

    public void setActivationAccountNumber(String activationAccountNumber) {
        this.activationAccountNumber = activationAccountNumber;
    }

    public String getTransactionUniqueId() {
        return transactionUniqueId;
    }

    public void setTransactionUniqueId(String transactionUniqueId) {
        this.transactionUniqueId = transactionUniqueId;
    }

    public String getCorrelatedTransactionUniqueId() {
        return correlatedTransactionUniqueId;
    }

    public void setCorrelatedTransactionUniqueId(
            String correlatedTransactionUniqueId) {
        this.correlatedTransactionUniqueId = correlatedTransactionUniqueId;
    }
}
