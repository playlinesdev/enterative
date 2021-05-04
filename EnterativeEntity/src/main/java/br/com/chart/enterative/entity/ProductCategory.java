package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Categoria do produto que é exibido na tela
 *
 * @author Cristhiano Roberto
 *
 */
@Entity
@Table(name = "product_category")
public class ProductCategory extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", unique = true, length = 100)
    @Size(max = 100, message = ENTITY_MESSAGE.SIZE_100)
    @NotBlank(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String code;

    @ManyToOne
    @JoinColumn(name = "product_category_parent")
    @Getter @Setter private ProductCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @Getter @Setter private List<ProductCategory> children;
}
