package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "epay_transaction")
public class EpayTransaction extends UserAwareEntity {

    private static final long serialVersionUID = 1L;
    
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
    @JoinColumn(name = "epay_activation_id")
    @Getter @Setter private EpayActivation activation;
    
    @Column(name = "amount")
    @Getter @Setter private String amount;

    @Column(name = "local_date_time")
    @Getter @Setter private String localDateTime;
    
    @Column(name = "message_number")
    @Getter @Setter private String messageNumber;
    
    @Column(name = "phone")
    @Getter @Setter private String phone;
    
    @Column(name = "product_id")
    @Getter @Setter private String productId;
    
    @Column(name = "request_type")
    @Getter @Setter private String requestType;
    
    @Column(name = "item_sequence")
    @Getter @Setter private String sequence;
    
    @Column(name = "service_fee")
    @Getter @Setter private String serviceFee;
    
    @Column(name = "terminal_id")
    @Getter @Setter private String terminalId;
    
    @Column(name = "transaction_id")
    @Getter @Setter private String transactionId;
    
    @Column(name = "result_code")
    @Getter @Setter private String resultCode;
    
    @Column(name = "result_text")
    @Getter @Setter private String resultText;
    
    @Column(name = "hosttxid")
    @Getter @Setter private String hostTXID;
    
    @Column(name = "receipt_customer")
    @Getter @Setter private String receiptCustomer;
    
    @Column(name = "receipt_merchant")
    @Getter @Setter private String receiptMerchant;
    
    @Column(name = "server_date_time")
    @Getter @Setter private String serverDateTime;
}
