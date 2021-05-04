package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bhn_transaction")
public class BHNTransaction extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    // +-----------------------------------------------------
    // | Extra Data
    // +-----------------------------------------------------
    @Column(name = "queue_insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date queueInsertDate;

    @Column(name = "transaction_return_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date transactionReturnDate;

    @Enumerated
    @Column(name = "direction")
    @Getter @Setter private TRANSACTION_DIRECTION direction;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    @Getter @Setter private Resource resource;

    @ManyToOne
    @JoinColumn(name = "bhn_activation_id")
    @Getter @Setter private BHNActivation bhnActivation;

    // +-----------------------------------------------------
    // | Header
    // +-----------------------------------------------------
    @Column(name = "signature", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)    
    @Getter @Setter private String signature;

    @Column(name = "product_category_code", length = 2)
    @Size(max = 2, message = ENTITY_MESSAGE.SIZE_2)
    @Getter @Setter private String productCategoryCode;

    @Column(name = "spec_version", length = 2)
    @Size(max = 2, message = ENTITY_MESSAGE.SIZE_2)
    @Getter @Setter private String specVersion;

    @Column(name = "status_code", length = 2)
    @Size(max = 2, message = ENTITY_MESSAGE.SIZE_2)
    @Getter @Setter private String statusCode;

    // +-----------------------------------------------------
    // | Transaction
    // +-----------------------------------------------------
    @Column(name = "acquiring_institution_identifier", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String acquiringInstitutionIdentifier;

    @Column(name = "local_transaction_date", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String localTransactionDate;

    @Column(name = "local_transaction_time", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String localTransactionTime;

    @Column(name = "merchant_category_code", length = 4)
    @Size(max = 4, message = ENTITY_MESSAGE.SIZE_4)
    @Getter @Setter private String merchantCategoryCode;

    @Column(name = "merchant_identifier", length = 15)
    @Size(max = 15, message = ENTITY_MESSAGE.SIZE_15)
    @Getter @Setter private String merchantIdentifier;

    @Column(name = "merchant_location", length = 40)
    @Size(max = 40, message = ENTITY_MESSAGE.SIZE_40)
    @Getter @Setter private String merchantLocation;

    @Column(name = "merchant_terminal_id", length = 16)
    @Size(max = 16, message = ENTITY_MESSAGE.SIZE_16)
    @Getter @Setter private String merchantTerminalId;

    @Column(name = "point_of_service_entry_mode", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String pointOfServiceEntryMode;

    @Column(name = "primary_account_number", length = 19)
    @Size(max = 19, message = ENTITY_MESSAGE.SIZE_19)
    @Getter @Setter private String primaryAccountNumber;

    @Column(name = "processing_code", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String processingCode;

    @Column(name = "retrieval_reference_number", length = 12)
    @Size(max = 12, message = ENTITY_MESSAGE.SIZE_12)
    @Getter @Setter private String retrievalReferenceNumber;

    @Column(name = "system_trace_audit_number", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String systemTraceAuditNumber;

    @Column(name = "transaction_amount", length = 12)
    @Size(max = 12, message = ENTITY_MESSAGE.SIZE_12)
    @Getter @Setter private String transactionAmount;

    @Column(name = "transaction_currency_code", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String transactionCurrencyCode;

    @Column(name = "transmission_date_time", length = 12)
    @Size(max = 12, message = ENTITY_MESSAGE.SIZE_12)
    @Getter @Setter private String transmissionDateTime;

    @Column(name = "auth_identification_response", length = 6)
    @Size(max = 6, message = ENTITY_MESSAGE.SIZE_6)
    @Getter @Setter private String authIdentificationResponse;

    @Column(name = "response_code", length = 2)
    @Size(max = 2, message = ENTITY_MESSAGE.SIZE_2)
    @Getter @Setter private String responseCode;

    @Column(name = "network_management_code", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String networkManagementCode;

    @Column(name = "terms_and_conditions", length = 999)
    @Size(max = 999, message = ENTITY_MESSAGE.SIZE_999)
    @Getter @Setter private String termsAndConditions;

    @Column(name = "product_id", length = 99)
    @Size(max = 99, message = ENTITY_MESSAGE.SIZE_99)
    @Getter @Setter private String productId;

    @Column(name = "balance_amount", length = 13)
    @Size(max = 13, message = ENTITY_MESSAGE.SIZE_13)
    @Getter @Setter private String balanceAmount;

    @Column(name = "redemption_pin", length = 16)
    @Size(max = 16, message = ENTITY_MESSAGE.SIZE_16)
    @Getter @Setter private String redemptionPin;

    @Column(name = "redemption_account_number", length = 30)
    @Size(max = 30, message = ENTITY_MESSAGE.SIZE_30)
    @Getter @Setter private String redemptionAccountNumber;

    @Column(name = "activation_account_number", length = 19)
    @Size(max = 19, message = ENTITY_MESSAGE.SIZE_19)
    @Getter @Setter private String activationAccountNumber;

    @Column(name = "transaction_unique_id", length = 26)
    @Size(max = 26, message = ENTITY_MESSAGE.SIZE_26)
    @Getter @Setter private String transactionUniqueId;

    @Column(name = "correlated_transaction_unique_id", length = 26)
    @Size(max = 26, message = ENTITY_MESSAGE.SIZE_26)
    @Getter @Setter private String correlatedTransactionUniqueId;

    @Column(name = "line", length = 680)
    @Size(max = 680, message = ENTITY_MESSAGE.SIZE_680)
    @Getter @Setter private String line;
}
