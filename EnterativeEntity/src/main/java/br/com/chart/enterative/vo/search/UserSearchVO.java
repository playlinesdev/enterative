package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class UserSearchVO extends NamedVO{

    private static final long serialVersionUID = 1L;

    private String login;
    private String email;
}
