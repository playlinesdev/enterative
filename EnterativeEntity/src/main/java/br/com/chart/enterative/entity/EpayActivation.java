package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "epay_activation", uniqueConstraints = @UniqueConstraint(columnNames = {"external_code"}))
public class EpayActivation extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private ACTIVATION_STATUS status;

    @Enumerated
    @Column(name = "queue_status")
    @Getter @Setter private ACTIVATION_QUEUE_STATUS queueStatus;

    @Enumerated
    @Column(name = "callback_status")
    @Getter @Setter private CALLBACK_STATUS callbackStatus;

    @Column(name = "response_code", length = 10)
    @Size(max = 10, message = ENTITY_MESSAGE.SIZE_10)
    @Getter @Setter private String responseCode;

    @Column(name = "ttl_activation")
    @Getter @Setter private Integer ttlActivation;
    
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @Getter @Setter private Merchant merchant;
    
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    @Getter @Setter private EpayVoucher voucher;
    
    @Column(name = "shop_code", length = 5)
    @Size(max = 5, message = ENTITY_MESSAGE.SIZE_5)
    @Getter @Setter private String shopCode;

    @Column(name = "terminal", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String terminal;

    @Column(name = "external_code", length = 20, unique = true)
    @Size(max = 20, message = ENTITY_MESSAGE.SIZE_20)
    @Getter @Setter private String externalCode;

    @Column(name = "callbackurl", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String callbackurl;
    
    @Column(name = "transaction_id", length = 36)
    @Size(max = 36, message = ENTITY_MESSAGE.SIZE_36)
    @Getter @Setter private String transactionId;
}
