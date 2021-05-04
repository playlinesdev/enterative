package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "supplier")
public class Supplier extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "spec_version", length = 50)
    @Size(max = 50, message = ENTITY_MESSAGE.SIZE_50)
    @Getter @Setter private String specVersion;

    @Column(name = "signature", length = 50)
    @Size(max = 50, message = ENTITY_MESSAGE.SIZE_50)
    @Getter @Setter private String signature;
}
