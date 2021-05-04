package br.com.chart.enterative.service.report;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.*;
import br.com.chart.enterative.helper.TimeMeasure;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.*;
import br.com.chart.enterative.vo.report.AccountBalanceCheckReportVO;
import br.com.chart.enterative.vo.report.AccountBalanceReportVO;
import br.com.chart.enterative.vo.report.ReportProgress;
import br.com.chart.enterative.vo.search.AccountBalanceCheckSearchVO;
import br.com.chart.enterative.vo.search.AccountBalanceSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author William Leite
 */
@Service
public class AccountBalanceCheckReportService extends UserAwareComponent {

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService accountTransactionDeadFileService;

    @Autowired
    private SaleOrderCRUDService saleOrderService;

    @Autowired
    private PurchaseOrderCRUDService purchaseOrderService;

    @Autowired
    private EnvParameterDAO parameterDAO;

    protected Map<Long, ReportProgress> progress = new HashMap<>();

    public Map<String, Object> assembleReportVariables(User user, AccountBalanceCheckSearchVO vo) throws ParseException {
        this.progress.put(user.getId(), new ReportProgress());

        ReportProgress progress = this.progress.get(user.getId());

        TimeMeasure total = new TimeMeasure().start();

        Map<String, Object> result = new HashMap<>(0);

        List<AccountBalanceCheckReportVO> data = new ArrayList<>(0);

        TimeMeasure t = new TimeMeasure().start();
        List<AccountVO> accounts = this.accountService.findAllVO().collect(Collectors.toList());
        progress.log(t.end("accounts"));

        progress.setProgressMax(new BigDecimal(accounts.size()));
        progress.setProgress(BigDecimal.ZERO);
        accounts.parallelStream().forEach(account -> {
            AccountBalanceCheckReportVO item = new AccountBalanceCheckReportVO();
            item.setName(account.getName());

            t.start();
            Specification<SaleOrder> saleOrderSpecification = this.saleOrderService.assembleSpecification(null, null, null, account.getId(), null, new SALE_ORDER_STATUS[]{SALE_ORDER_STATUS.ACTIVATED, SALE_ORDER_STATUS.PENDING}, null);
            List<SaleOrder> saleOrders = this.saleOrderService.retrieveOrders(saleOrderSpecification);

            item.setApprovedSale(BigDecimal.ZERO);
            item.setPendingSale(BigDecimal.ZERO);
            saleOrders.forEach(order -> {
                if (order.getStatus() == SALE_ORDER_STATUS.PENDING) {
                    item.setPendingSale(item.getPendingSale().add(order.getAmount()));
                } else if (order.getStatus() == SALE_ORDER_STATUS.ACTIVATED) {
                    item.setApprovedSale(item.getApprovedSale().add(order.getAmount()));
                }
            });
            progress.log(t.end("sale - " + account.getName()));

            t.start();
            Specification<PurchaseOrder> purchaseOrderSpecification = this.purchaseOrderService.assembleSpecification(account.getId(), PURCHASE_ORDER_STATUS.ACTIVE, true);
            List<PurchaseOrder> purchaseOrders = this.purchaseOrderService.retrieveOrders(purchaseOrderSpecification);
            item.setCommission(purchaseOrders.stream().map(PurchaseOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

            purchaseOrderSpecification = this.purchaseOrderService.assembleSpecification(account.getId(), PURCHASE_ORDER_STATUS.ACTIVE, false);
            purchaseOrders = this.purchaseOrderService.retrieveOrders(purchaseOrderSpecification);
            item.setDeposit(purchaseOrders.stream().map(PurchaseOrder::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            progress.log(t.end("purchase - " + account.getName()));

            t.start();
            BigDecimal credit = item.getDeposit().add(item.getCommission());
            BigDecimal debit = item.getApprovedSale().add(item.getPendingSale());
            item.setCalculatedBalance(credit.subtract(debit));
            item.setBalance(this.accountTransactionService.retrieveAccountBalance(account.getId()));
            progress.log(t.end("balance - " + account.getName()));

            t.start();
            Long categoryId = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_REFUND);
            Specification<AccountTransaction> accountTransactionSpecification = this.accountTransactionService.assembleSpecification(null, null, account.getId(), categoryId, ACCOUNT_TRANSACTION_STATUS.ACTIVE, null);
            List<AccountTransaction> transactions = this.accountTransactionService.retrieveTransactions(accountTransactionSpecification);
            BigDecimal totalTransactions = transactions.stream().map(tr -> tr.getType() == ACCOUNT_TRANSACTION_TYPE.DEBIT ? tr.getAmount().negate() : tr.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

            Specification<AccountTransactionDeadFile> accountTransactionDeadFileSpecification = this.accountTransactionDeadFileService.assembleSpecification(null, null, account.getId(), categoryId, ACCOUNT_TRANSACTION_STATUS.ACTIVE, null);
            List<AccountTransactionDeadFile> accountTransactionDeadFiles = this.accountTransactionDeadFileService.retrieveTransactions(accountTransactionDeadFileSpecification);
            BigDecimal totalDeadTransactions = accountTransactionDeadFiles.stream().map(tr -> tr.getType() == ACCOUNT_TRANSACTION_TYPE.DEBIT ? tr.getAmount().negate() : tr.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

            item.setRefund(totalDeadTransactions.add(totalTransactions));
            progress.log(t.end("refund - " + account.getName()));

            item.setDifferent(item.getCalculatedBalance().setScale(2, RoundingMode.HALF_UP).compareTo(item.getBalance().setScale(2, RoundingMode.HALF_UP).add(item.getRefund())) != 0);

            progress.setProgress(progress.getProgress().add(BigDecimal.ONE));

            data.add(item);
        });

        total.end("total");

        if (vo.isRemoveEmpty()) {
            data.removeIf(v -> v.getApprovedSale().signum() == 0 && v.getPendingSale().signum() == 0 && v.getCommission().signum() == 0
                    && v.getDeposit().signum() == 0 && v.getBalance().signum() == 0 && v.getCalculatedBalance().signum() == 0);
        }

        data.sort(Comparator.comparing(AccountBalanceCheckReportVO::getName));

        result.put("objectList", data);

        progress.setDone(true);
        return result;
    }

    public ReportProgress getProgress(User user) {
        ReportProgress progress = this.progress.getOrDefault(user.getId(), new ReportProgress());
        progress.setLastUpdate(LocalDateTime.now());
        ReportProgress p = progress.clone();
        progress.getMessages().clear();
        return p;
    }
}
