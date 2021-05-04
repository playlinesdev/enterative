package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.BaseEntity;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "account_transaction_dead_file")
public class AccountTransactionDeadFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter @Setter protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @Getter @Setter protected Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "altered_at")
    @Getter @Setter protected Date alteredAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @Getter @Setter protected User createdBy;

    @ManyToOne
    @JoinColumn(name = "altered_by")
    @Getter @Setter protected User alteredBy;

    @Column(name = "name")
    @Getter @Setter protected String name;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;

    @Enumerated
    @Column(name = "type")
    @Getter @Setter private ACCOUNT_TRANSACTION_TYPE type;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private ACCOUNT_TRANSACTION_STATUS status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    @Getter @Setter private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "sale_order_line_id")
    @Getter @Setter private SaleOrderLine saleOrderLine;

    @ManyToOne
    @JoinColumn(name = "purchase_order_line_id")
    @Getter @Setter private PurchaseOrderLine purchaseOrderLine;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @Getter @Setter private Account account;

    @ManyToOne
    @JoinColumn(name = "account_transaction_category_id")
    @Getter @Setter private AccountTransactionCategory category;
}
