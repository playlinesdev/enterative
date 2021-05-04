package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "sale_order")
public class SaleOrder extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @Getter @Setter private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private SALE_ORDER_STATUS status;

    @Enumerated
    @Column(name = "type")
    @Getter @Setter private SALE_ORDER_TYPE type;

    @Enumerated
    @Column(name = "email_status")
    @Getter @Setter private EMAIL_STATUS emailStatus;

    @Column(name = "customer_mobile", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String customerMobile;

    @Column(name = "payment_manager_token", length = 36)
    @Size(max = 36, message = ENTITY_MESSAGE.SIZE_36)
    @Getter @Setter private String paymentManagerToken;

    @Column(name = "payment_gateway_token", length = 36)
    @Size(max = 36, message = ENTITY_MESSAGE.SIZE_36)
    @Getter @Setter private String paymentGatewayToken;

    @Column(name = "payment_transaction_id", length = 36)
    @Size(max = 36, message = ENTITY_MESSAGE.SIZE_36)
    @Getter @Setter private String paymentTransactionId;
    
    @Column(name = "locale", length = 255)
    @Getter @Setter private String locale;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter @Setter private List<SaleOrderLine> lines;

    @Column(name = "receipt_count")
    @Getter @Setter private Integer receiptCount;
}
