package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "partner")
public class Partner extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "partner_product",
            joinColumns = @JoinColumn(table = "partner", name = "partner_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(table = "product", name = "product_id", referencedColumnName = "id"))
    @Getter @Setter private List<Product> products;
}
