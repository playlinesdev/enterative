package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "shop_product_commission")
public class ShopProductCommission extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter @Setter private Product product;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;

    @Column(name = "percentage")
    @NumberFormat(pattern = "###.00")
    @Getter @Setter private BigDecimal percentage;
}
