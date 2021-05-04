package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "merchant_category")
public class MerchantCategory extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 4)
    @Size(max = 4, message = ENTITY_MESSAGE.SIZE_4)
    @NotEmpty(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String code;
}
