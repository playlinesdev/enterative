package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "shopping_cart")
public class ShoppingCart extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    @Getter @Setter private SaleOrder saleOrder;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter private List<ShoppingCartLine> lines;
}
