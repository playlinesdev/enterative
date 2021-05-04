package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.SHOP_PHONE_TYPE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "shop_phone")
public class ShopPhone extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;

    @Column(name = "phone", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String phone;

    @Enumerated
    @Column(name = "type")
    @Getter @Setter private SHOP_PHONE_TYPE type;

    @Column(name = "contact", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String contact;
}
