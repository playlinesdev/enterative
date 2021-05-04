package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "purchase_order_line")
public class PurchaseOrderLine extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    @NotNull(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private PurchaseOrder purchaseOrder;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private PURCHASE_ORDER_STATUS status;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @NotNull(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private BigDecimal amount;
}
