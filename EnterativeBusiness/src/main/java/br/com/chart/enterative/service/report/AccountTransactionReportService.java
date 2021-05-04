package br.com.chart.enterative.service.report;

import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTransactionSearchGroupingVO;
import br.com.chart.enterative.vo.search.AccountTransactionSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author William Leite
 */
@Service
public class AccountTransactionReportService extends UserAwareComponent {

    @Autowired
    private AccountTransactionCRUDService transactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService deadFileService;

    public Map<String, Object> assembleTransactionReportVariables(User user, AccountTransactionSearchVO vo) {
        Map<String, Object> variables = new LinkedHashMap<>();

        List<AccountTransactionVO> transactions = this.transactionService.retrieveTransactions(user, vo);
        List<AccountTransactionVO> deadFileTransactions = this.deadFileService.retrieveTransactions(user, vo);

        List<AccountTransactionVO> objectList = new ArrayList<>();
        Predicate<AccountTransactionVO> filter = t -> Objects.nonNull(t.getSaleOrderNumber()) || Objects.nonNull(t.getPurchaseOrderNumber());
        objectList.addAll(transactions.stream().filter(filter).collect(Collectors.toList()));
        objectList.addAll(deadFileTransactions.stream().filter(filter).collect(Collectors.toList()));
        objectList = objectList.stream().peek(v -> {
            if (Objects.nonNull(v.getProductName()) && v.getProductName().length() > 60) {
                v.setProductName(v.getProductName().substring(0, 60) + " (...)");
            }
        }).collect(Collectors.toList());

        if (Arrays.stream(vo.getGrouping()).noneMatch(AccountTransactionSearchGroupingVO::isSelected)) {
            variables.put("objectList", objectList);
            variables.put("grouping", false);
        } else {
            Map<String, Object> hm = this.transactionService.groupList(objectList, vo.getGrouping());
            variables.put("objectList", hm);
            Map<String, Object> hmTotal = this.transactionService.totalGroupList(hm);
            variables.put("objectListTotals", hmTotal);
            variables.put("grouping", true);
        }

        ServiceResponse response = this.transactionService.retrieveTotals(objectList);
        if (Objects.isNull(response.getMessage())) {
            variables.put("totals", response.get("totals"));
        }

        variables.put("reportType", vo.getReportType());

        return variables;
    }

    public Map<String, Object> assembleExcerptReportVariables(User user, AccountTransactionSearchVO vo) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("searchForm", vo);

        List<AccountTransactionVO> transactions = this.transactionService.retrieveTransactions(user, vo);
        List<AccountTransactionVO> deadFileTransactions = this.deadFileService.retrieveTransactions(user, vo);
        if (Objects.nonNull(deadFileTransactions)) {
            transactions.addAll(deadFileTransactions);
        }

        final int maxProductNameWidth = 60;
        final AtomicReference<BigDecimal> currentValue = new AtomicReference<>(BigDecimal.ZERO);

        transactions = transactions.stream()
                .filter(t -> Objects.nonNull(t.getPurchaseOrderNumber()) || Objects.nonNull(t.getSaleOrderNumber()))
                .peek(t -> {
                    if (Objects.nonNull(t.getProductName()) && t.getProductName().length() > maxProductNameWidth) {
                        t.setProductName(t.getProductName().substring(0, maxProductNameWidth - 1) + " (...)");
                    }
                })
                .sorted(Comparator.comparing(AccountTransactionVO::getTransactionDate))
                .peek(t -> {
                    if (t.getStatus() != ACCOUNT_TRANSACTION_STATUS.CANCELED) {
                        if (t.getType() == ACCOUNT_TRANSACTION_TYPE.CREDIT) {
                            currentValue.accumulateAndGet(t.getAmount(), BigDecimal::add);
                        } else {
                            currentValue.accumulateAndGet(t.getAmount(), BigDecimal::subtract);
                        }
                    }
                    t.setCurrentValue(currentValue.get());
                })
                .collect(Collectors.toList());

        Long accountID = this.retrieveAccountID(user, vo.getAccount());
        if (Objects.nonNull(accountID)) {
            transactions.add(0, this.transactionService.retrieveLastPositionVO(accountID, vo, true));
        }

        result.put("transactions", transactions);

        ServiceResponse response = this.transactionService.retrieveTotals(transactions);
        if (Objects.isNull(response.getMessage())) {
            Map<ACCOUNT_TRANSACTION_TYPE, Map<ACCOUNT_TRANSACTION_STATUS, BigDecimal>> totals = response.get("totals");

            BigDecimal canceledDebit = totals.get(ACCOUNT_TRANSACTION_TYPE.DEBIT).get(ACCOUNT_TRANSACTION_STATUS.CANCELED);
            result.put("canceled_debit", canceledDebit);
            BigDecimal pendingDebit = totals.get(ACCOUNT_TRANSACTION_TYPE.DEBIT).get(ACCOUNT_TRANSACTION_STATUS.PENDING);
            result.put("pending_debit", pendingDebit);
            BigDecimal activeDebit = totals.get(ACCOUNT_TRANSACTION_TYPE.DEBIT).get(ACCOUNT_TRANSACTION_STATUS.ACTIVE);
            result.put("active_debit", activeDebit);

            BigDecimal canceledCredit = totals.get(ACCOUNT_TRANSACTION_TYPE.CREDIT).get(ACCOUNT_TRANSACTION_STATUS.CANCELED);
            result.put("canceled_credit", canceledCredit);
            BigDecimal pendingCredit = totals.get(ACCOUNT_TRANSACTION_TYPE.CREDIT).get(ACCOUNT_TRANSACTION_STATUS.PENDING);
            result.put("pending_credit", pendingCredit);
            BigDecimal activeCredit = totals.get(ACCOUNT_TRANSACTION_TYPE.CREDIT).get(ACCOUNT_TRANSACTION_STATUS.ACTIVE);
            result.put("active_credit", activeCredit);

            BigDecimal canceledTotal = canceledCredit.subtract(canceledDebit);
            result.put("canceled_total", canceledTotal);
            BigDecimal pendingTotal = pendingCredit.subtract(pendingDebit);
            result.put("pending_total", pendingTotal);
            BigDecimal activeTotal = activeCredit.subtract(activeDebit);
            result.put("active_total", activeTotal);
            BigDecimal total = activeCredit.add(pendingCredit).subtract(activeDebit).subtract(pendingDebit);
            result.put("total", total);
        }

        return result;
    }
}
