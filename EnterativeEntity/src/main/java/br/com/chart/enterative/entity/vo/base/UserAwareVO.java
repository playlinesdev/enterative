package br.com.chart.enterative.entity.vo.base;

import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author William Leite
 */
@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
@ToString
public abstract class UserAwareVO extends NamedVO {

    @Getter @Setter private Date createdAt;
    @Getter @Setter private Date alteredAt;
    @Getter @Setter private UserVO createdBy;
    @Getter @Setter private UserVO alteredBy;
}
