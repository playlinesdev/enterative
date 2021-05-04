package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "shop_product_commission_template_line")
public class ShopProductCommissionTemplateLine extends UserAwareEntity {

    @ManyToOne
    @JoinColumn(name = "template_id")
    @Getter @Setter private ShopProductCommissionTemplate template;

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
