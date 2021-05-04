package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class UserRoleVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;
    @Getter @Setter private String name;
    // UI
    @Getter @Setter private boolean selected;
}
