package br.com.chart.enterative.service.report;

import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.converter.ReportConverterService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import br.com.chart.enterative.vo.report.ClientCommissionReportVO;
import br.com.chart.enterative.vo.search.AccountCommissionSearchVO;
import java.text.ParseException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountCommissionReportService extends UserAwareComponent {

    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;

    @Autowired
    private AccountTransactionDeadFileCRUDService accountTransactionDeadFileCRUDService;

    @Autowired
    private ReportConverterService reportConverterService;

    public Map<String, Object> assembleReportVariables(User user, AccountCommissionSearchVO vo) throws ParseException {
        Map<String, Object> result = new HashMap<>();

        List<ClientCommissionReportVO> data = new ArrayList<>();

        List<AccountTransaction> transactions = this.accountTransactionCRUDService.retrieveTransactions(user, vo);
        transactions.stream().map(this.reportConverterService::toClientCommissionReportVO).forEach(data::add);

        List<AccountTransactionDeadFile> deadFileTransactions = this.accountTransactionDeadFileCRUDService.retrieveTransactions(user, vo);
        deadFileTransactions.stream().map(this.reportConverterService::toClientCommissionReportVO).forEach(data::add);
        data.sort(Comparator.comparing(ClientCommissionReportVO::getDate));
        result.put("commissions", data);

        return result;
    }
}
