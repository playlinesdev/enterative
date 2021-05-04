package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class BHNTransactionVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    // +-----------------------------------------------------
    // | Extra Data
    // +-----------------------------------------------------
    @Getter @Setter private Date queueInsertDate;
    @Getter @Setter private Date transactionReturnDate;
    @Getter @Setter private TRANSACTION_DIRECTION direction;
    @Getter @Setter private ResourceVO resource;
    @Getter @Setter private BHNActivationVO bhnActivation;

    // +-----------------------------------------------------
    // | Header
    // +-----------------------------------------------------
    @Getter @Setter private String signature;
    @Getter @Setter private String productCategoryCode;
    @Getter @Setter private String specVersion;
    @Getter @Setter private String statusCode;

    // +-----------------------------------------------------
    // | Transaction
    // +-----------------------------------------------------
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
    @Getter @Setter private String productId;
    @Getter @Setter private String balanceAmount;
    @Getter @Setter private String redemptionPin;
    @Getter @Setter private String redemptionAccountNumber;
    @Getter @Setter private String activationAccountNumber;
    @Getter @Setter private String transactionUniqueId;
    @Getter @Setter private String correlatedTransactionUniqueId;
    @Getter @Setter private String line;
    @Getter @Setter private List<String> discrepancies;
}
