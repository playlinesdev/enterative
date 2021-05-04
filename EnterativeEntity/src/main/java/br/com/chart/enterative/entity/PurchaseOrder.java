package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @Getter @Setter private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;

    @Column(name = "total_amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal totalAmount;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private PURCHASE_ORDER_STATUS status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activated_web_user_id")
    @Getter @Setter private User activatedUser;

    @Column(name = "activated_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date activatedDate;

    @OneToMany(mappedBy = "purchaseOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter private List<PurchaseOrderLine> lines;
}
