package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import javax.persistence.Column;
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
@Entity
@Table(name = "product_highlight")
public class ProductHighlight extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter @Setter private Product product;

    @Column(name = "highlight_sequence")
    @Getter @Setter private Integer sequence;
}
