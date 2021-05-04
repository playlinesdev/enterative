package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.REPORT_TYPE;
import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author William Leite
 */
public class AccountTransactionSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private AccountVO account;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date endDate;
    @Getter @Setter private ACCOUNT_TRANSACTION_TYPE type;
    @Getter @Setter private ACCOUNT_TRANSACTION_STATUS status;
    @Getter @Setter private AccountTransactionCategoryVO category;
    @Getter @Setter private AccountTransactionSearchGroupingVO[] grouping;
    @Getter @Setter private REPORT_TYPE reportType;
}
