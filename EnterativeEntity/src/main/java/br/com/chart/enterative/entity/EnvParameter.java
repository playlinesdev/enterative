package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "env_parameter")
public class EnvParameter extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "param_value", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String value;

    @Enumerated
    @Column(name = "param")
    @Getter @Setter private ENVIRONMENT_PARAMETER param;
}
