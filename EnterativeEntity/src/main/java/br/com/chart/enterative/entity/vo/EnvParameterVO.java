package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class EnvParameterVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ENVIRONMENT_PARAMETER param;
    @Getter @Setter private String value;
    @Getter @Setter private String name;
}
