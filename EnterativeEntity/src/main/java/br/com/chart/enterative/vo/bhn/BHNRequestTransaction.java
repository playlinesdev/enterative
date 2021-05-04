package br.com.chart.enterative.vo.bhn;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BHNRequestTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String acquiringInstitutionIdentifier;
    @Getter @Setter private String localTransactionDate;
    @Getter @Setter private String localTransactionTime;
    @Getter @Setter private String merchantCategoryCode;
    @Getter @Setter private String merchantIdentifier;
    @Getter @Setter private String merchantLocation;
    @Getter @Setter private String merchantTerminalId;
    @Getter @Setter private String pointOfServiceEntryMode;
    @Getter @Setter private String primaryAccountNumber;
    @Getter @Setter private String processingCode;
    @Getter @Setter private String retrievalReferenceNumber;
    @Getter @Setter private String systemTraceAuditNumber;
    @Getter @Setter private String transactionAmount;
    @Getter @Setter private String transactionCurrencyCode;
    @Getter @Setter private String transmissionDateTime;
    @Getter @Setter private String authIdentificationResponse;
    @Getter @Setter private String responseCode;
    @Getter @Setter private String networkManagementCode;
    @Getter @Setter private String termsAndConditions;
    @Getter @Setter private BHNRequestReceiptsFields receiptsFields;
    @Getter @Setter private BHNRequestAdditionalTxnFields additionalTxnFields;

    public BHNRequestTransaction() {
        this.receiptsFields = new BHNRequestReceiptsFields();
        this.additionalTxnFields = new BHNRequestAdditionalTxnFields();
    }
}
