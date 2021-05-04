package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class AccountSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private STATUS status;
    @Getter @Setter private AccountTypeVO type;

}
