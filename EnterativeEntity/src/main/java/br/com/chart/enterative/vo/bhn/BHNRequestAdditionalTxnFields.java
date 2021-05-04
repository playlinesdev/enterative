package br.com.chart.enterative.vo.bhn;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BHNRequestAdditionalTxnFields implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String productId;
    @Getter @Setter private String balanceAmount;
    @Getter @Setter private String redemptionPin;
    @Getter @Setter private String redemptionAccountNumber;
    @Getter @Setter private String activationAccountNumber;
    @Getter @Setter private String transactionUniqueId;
    @Getter @Setter private String correlatedTransactionUniqueId;
}
