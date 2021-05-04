package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity()
@Table(name = "cielo_shop_exception")
public class CieloShopException extends UserAwareEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;
}