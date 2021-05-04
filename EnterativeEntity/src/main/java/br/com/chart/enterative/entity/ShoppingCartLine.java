package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "shopping_cart_line")
public class ShoppingCartLine extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    @Getter @Setter private ShoppingCart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter @Setter private Product product;

    @Column(name = "quantity")
    @NumberFormat(pattern = "###,###,###")
    @Getter @Setter private BigDecimal quantity;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;

    @Column(name = "total_amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal totalAmount;
        
    @Column(name = "storestep")
    @Getter @Setter private String storeStep;
    
    @Column(name = "user_email")
    @Getter @Setter private String userEmail;
    
    @Column(name = "user_cellphone")
    @Getter @Setter private String userCellphone;
}
