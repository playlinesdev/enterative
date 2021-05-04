package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.STATUS;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class AccountVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    private List<UserVO> users;
    private List<ShopVO> shops;
    private List<AccountTransactionVO> transactions;
    private STATUS status;
    private AccountTypeVO type;
    private AccountVO parent;
    private BigDecimal balanceThreshold;
    // UI
    private BigDecimal balance;
}
