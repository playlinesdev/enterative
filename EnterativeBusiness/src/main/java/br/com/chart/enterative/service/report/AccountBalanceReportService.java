package br.com.chart.enterative.service.report;

import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.vo.report.AccountBalanceReportVO;
import br.com.chart.enterative.vo.search.AccountBalanceSearchVO;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountBalanceReportService extends UserAwareComponent {

    @Autowired
    private AccountCRUDService accountService;
    
    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    public Map<String, Object> assembleReportVariables(User user, AccountBalanceSearchVO vo) throws ParseException {
        Map<String, Object> result = new HashMap<>(0);

        List<AccountBalanceReportVO> data = new ArrayList<>(0);
        
        List<AccountVO> accounts = this.accountService.findByStatusOrderByName(STATUS.ACTIVE);
        accounts.stream().forEach(account -> {
            AccountBalanceReportVO item = new AccountBalanceReportVO();
            item.setName(account.getName());
            item.setBalance(this.accountTransactionService.retrieveAccountBalance(account.getId()).setScale(2, RoundingMode.DOWN));
            data.add(item);
        });

        result.put("objectList", data);

        return result;
    }
}
