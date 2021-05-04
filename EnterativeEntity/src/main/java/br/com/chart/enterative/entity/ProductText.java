package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "product_text")
public class ProductText extends UserAwareEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "description")
    @Getter @Setter private String description;
    
    @Enumerated
    @Column(name = "type")
    @Getter @Setter private PRODUCT_TEXT_TYPE type;
}
