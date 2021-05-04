package br.com.chart.enterative.service.report;

import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.REPORT_DATE_RANGE;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.*;
import br.com.chart.enterative.vo.report.SalesByProductItemReportVO;
import br.com.chart.enterative.vo.report.SalesByProductReportVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchColumnVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
@Service
public class SalesByAccountReportService extends UserAwareComponent {

    @Autowired
    private AccountTransactionCRUDService transactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService deadTransactionService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private AccountCRUDService accountCRUDService;

    @Autowired
    private SaleOrderCRUDService saleOrderService;

    @Autowired
    private ProductCRUDService productCRUDService;

    public SalesByProductSearchVO initSearchVO() {
        SalesByProductSearchVO vo = new SalesByProductSearchVO();
        vo.setColumns(new SalesByProductSearchColumnVO[]{
                new SalesByProductSearchColumnVO(1L, "base.quantity"),
                new SalesByProductSearchColumnVO(2L, "base.amount"),
                new SalesByProductSearchColumnVO(3L, "base.chartcommission"),
                new SalesByProductSearchColumnVO(4L, "base.accountcommission"),
                new SalesByProductSearchColumnVO(5L, "base.profit")
        });
        return vo;
    }

    @Transactional
    public Map<String, Object> assembleReportVariables(SalesByProductSearchVO searchForm) {
        if (Objects.isNull(searchForm.getStartDate())) {
            searchForm.setStartDate(this.utils.fromLocalDate(this.utils.toLocalDate(new Date()).minusYears(1L)));
        }
        if (Objects.isNull(searchForm.getEndDate())) {
            searchForm.setEndDate(new Date());
        }

        Map<String, Object> result = new HashMap<>();

        Predicate<AccountTransactionVO> initialPredicate = (AccountTransactionVO t) -> {
            return t.getStatus() == ACCOUNT_TRANSACTION_STATUS.ACTIVE && t.getType() == ACCOUNT_TRANSACTION_TYPE.DEBIT;
        };

        List<AccountTransactionVO> transactions = this.transactionService.retrieveTransactions(searchForm).stream().filter(initialPredicate).collect(Collectors.toList());
        transactions.addAll(this.deadTransactionService.retrieveTransactions(searchForm).stream().filter(initialPredicate).collect(Collectors.toList()));

        List<AccountVO> accounts = this.accountCRUDService.findAllVO().sorted(Comparator.comparing(AccountVO::getName)).collect(Collectors.toList());
        List<SalesByProductReportVO> list = new ArrayList<>();

        REPORT_DATE_RANGE dateRange = this.utils.dateRange(searchForm.getStartDate(), searchForm.getEndDate());
        List<String> keys = this.utils.dateKeys(searchForm.getStartDate(), searchForm.getEndDate(), dateRange, this.locale());

        accounts.stream().forEach(a -> {
            SalesByProductReportVO vo = new SalesByProductReportVO(a);

            Predicate<AccountTransactionVO> basePredicate = (AccountTransactionVO t) -> {
                return Objects.equals(t.getAccount().getId(), a.getId());
            };

            keys.stream().forEach(k -> {
                final SalesByProductItemReportVO item = new SalesByProductItemReportVO();

                Predicate<AccountTransactionVO> datePredicate = (AccountTransactionVO t) -> {
                    switch (dateRange) {
                        case DAILY:
                            final SimpleDateFormat dailySDF = new SimpleDateFormat("dd/MM");
                            return Objects.equals(dailySDF.format(t.getTransactionDate()), k);
                        case WEEKLY:
                            WeekFields weekFields = WeekFields.of(this.locale());
                            final SimpleDateFormat weeklySDF = new SimpleDateFormat("dd/MM");
                            LocalDate date = this.utils.toLocalDate(t.getTransactionDate());
                            String week = String.valueOf(date.get(weekFields.weekOfWeekBasedYear()));
                            Date sunday = this.utils.fromLocalDate(date.with(weekFields.dayOfWeek(), 1));
                            Date saturday = this.utils.fromLocalDate(date.with(weekFields.dayOfWeek(), 7));
                            String match = String.format("%s - %s (%s)", weeklySDF.format(sunday), weeklySDF.format(saturday), week);
                            return Objects.equals(match, k);
                        case MONTHLY:
                            final SimpleDateFormat monthlySDF = new SimpleDateFormat("MMM/yy");
                            return Objects.equals(monthlySDF.format(t.getTransactionDate()), k);
                    }
                    return false;
                };

                transactions.stream().filter(basePredicate).filter(datePredicate).forEach(t -> {
                    item.setAmount(item.getAmount().add(t.getAmount()));
                    item.setQuantity(item.getQuantity().add(BigDecimal.ONE));

                    Product product = this.productCRUDService.findOne(t.getProductId());
                    BigDecimal chartCommission = this.retrieveValue(t.getAmount(), product.getCommissionAmount(), product.getCommissionPercentage().multiply(new BigDecimal(100)));
                    BigDecimal accountCommission = this.retrieveValue(t.getAmount(), t.getCommissionAmount(), t.getCommissionPercentage());
                    BigDecimal profit = chartCommission.subtract(accountCommission);

                    item.setChartCommission(item.getChartCommission().add(chartCommission));
                    item.setAccountCommission(item.getAccountCommission().add(accountCommission));
                    item.setProfit(item.getProfit().add(profit));

                    if (Objects.nonNull(t.getSaleOrderNumber())) {
                        SaleOrder order = this.saleOrderService.findOne(t.getSaleOrderNumber());
                        if (Objects.nonNull(order)) {
                            // External Payment
                            if (Objects.nonNull(order.getPaymentManagerToken()) && !order.getPaymentManagerToken().isEmpty()) {
                                if (Objects.nonNull(order.getPaymentTransactionId()) && !order.getPaymentTransactionId().isEmpty()) {
                                    vo.getPayments().get("PAGSEGURO").setAmount(vo.getPayments().get("PAGSEGURO").getAmount().add(t.getAmount()));
                                } else {
                                    vo.getPayments().get("CIELO").setAmount(vo.getPayments().get("PAGSEGURO").getAmount().add(t.getAmount()));
                                }
                            } else {
                                vo.getPayments().get("ENTERATIVEPAY").setAmount(vo.getPayments().get("ENTERATIVEPAY").getAmount().add(t.getAmount()));
                            }
                        }
                    }

                    vo.setAmount(vo.getAmount().add(t.getAmount()));
                    vo.setChartCommission(vo.getChartCommission().add(chartCommission));
                    vo.setProfit(vo.getProfit().add(profit));
                    vo.setQuantity(vo.getQuantity().add(BigDecimal.ONE));
                    vo.setAccountCommission(vo.getAccountCommission().add(accountCommission));
                });

                vo.getItems().put(k, item);
            });

            if (vo.getAmount().signum() > 0) {
                list.add(vo);
            }
        });

        result.put("keys", keys);
        result.put("objectList", list.toArray());
        return result;
    }

    private BigDecimal retrieveValue(BigDecimal base, BigDecimal amount, BigDecimal percentage) {
        if (Objects.nonNull(amount)) {
            return amount;
        } else if (Objects.nonNull(percentage)) {
            return base.multiply(percentage.divide(new BigDecimal(100)));
        } else {
            return BigDecimal.ZERO;
        }
    }
}
