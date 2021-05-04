package br.com.chart.enterative.service.report;

import br.com.chart.enterative.dao.ShopProductCommissionDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.SaleOrderLineVO;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.REPORT_DATE_RANGE;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.vo.report.SalesByProductItemReportVO;
import br.com.chart.enterative.vo.report.SalesByProductReportVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchColumnVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SalesByProductReportService extends UserAwareComponent {

    @Autowired
    private AccountTransactionCRUDService transactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService deadTransactionService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private ProductCRUDService productCRUDService;
    
    @Autowired
    private SaleOrderCRUDService saleOrderService;

    @Autowired
    private ShopProductCommissionDAO shopProductCommissionDAO;

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

    public Map<String, Object> assembleReportVariables(SalesByProductSearchVO searchForm) {
        if (Objects.isNull(searchForm.getStartDate())) {
            searchForm.setStartDate(this.utils.fromLocalDate(this.utils.toLocalDate(new Date()).minusYears(1L)));
        }
        if (Objects.isNull(searchForm.getEndDate())) {
            searchForm.setEndDate(new Date());
        }

        Map<String, Object> result = new HashMap<>();

        List<SaleOrder> orders = this.saleOrderService.retrieveOrders(searchForm);
        List<ProductVO> products = this.productCRUDService.findAllVO().collect(Collectors.toList());
        List<SalesByProductReportVO> list = new ArrayList<>();

        REPORT_DATE_RANGE dateRange = this.utils.dateRange(searchForm.getStartDate(), searchForm.getEndDate());
        List<String> keys = this.utils.dateKeys(searchForm.getStartDate(), searchForm.getEndDate(), dateRange, this.locale());

        products.forEach(p -> {
            SalesByProductReportVO vo = new SalesByProductReportVO(p);

            Predicate<SaleOrderLine> basePredicate = (SaleOrderLine l) -> Objects.equals(l.getProduct().getId(), p.getId());

            keys.forEach(k -> {
                final SalesByProductItemReportVO item = new SalesByProductItemReportVO();

                Predicate<SaleOrder> datePredicate = (SaleOrder s) -> {
                    switch (dateRange) {
                        case DAILY:
                            final SimpleDateFormat dailySDF = new SimpleDateFormat("dd/MM");
                            return Objects.equals(dailySDF.format(s.getCreatedAt()), k);
                        case WEEKLY:
                            WeekFields weekFields = WeekFields.of(this.locale());
                            final SimpleDateFormat weeklySDF = new SimpleDateFormat("dd/MM");
                            LocalDate date = this.utils.toLocalDate(s.getCreatedAt());
                            String week = String.valueOf(date.get(weekFields.weekOfWeekBasedYear()));
                            Date sunday = this.utils.fromLocalDate(date.with(weekFields.dayOfWeek(), 1));
                            Date saturday = this.utils.fromLocalDate(date.with(weekFields.dayOfWeek(), 7));
                            String match = String.format("%s - %s (%s)", weeklySDF.format(sunday), weeklySDF.format(saturday), week);
                            return Objects.equals(match, k);
                        case MONTHLY:
                            final SimpleDateFormat monthlySDF = new SimpleDateFormat("MMM/yy");
                            return Objects.equals(monthlySDF.format(s.getCreatedAt()), k);
                    }
                    return false;
                };

                orders.stream().filter(datePredicate).forEach(order -> {
                    order.getLines().stream().filter(basePredicate).forEach(line -> {
                        item.setAmount(item.getAmount().add(line.getAmount()));
                        item.setQuantity(item.getQuantity().add(BigDecimal.ONE));

                        BigDecimal commissionAmount = null;
                        BigDecimal commissionPercentage = null;

                        Shop shop = order.getShop();
                        if (Objects.nonNull(shop)) {
                            ShopProductCommission commission = this.shopProductCommissionDAO.findByShopAndProduct(shop, line.getProduct());
                            if (Objects.nonNull(commission)) {
                                if (Objects.nonNull(commission.getAmount()) && commission.getAmount().signum() > 0) {
                                    commissionAmount = commission.getAmount();
                                } else if (Objects.nonNull(commission.getPercentage()) && commission.getPercentage().signum() > 0) {
                                    commissionPercentage = commission.getPercentage().multiply(new BigDecimal(100));
                                }
                            } else {
                                commissionAmount = BigDecimal.ZERO;
                                commissionPercentage = BigDecimal.ZERO;
                            }
                        }

                        BigDecimal chartCommission = this.retrieveValue(line.getAmount(), line.getProduct().getCommissionAmount(), line.getProduct().getCommissionPercentage().multiply(new BigDecimal(100)));
                        BigDecimal accountCommission = this.retrieveValue(line.getAmount(), commissionAmount, commissionPercentage);
                        BigDecimal profit = chartCommission.subtract(accountCommission);

                        item.setChartCommission(item.getChartCommission().add(chartCommission));
                        item.setAccountCommission(item.getAccountCommission().add(accountCommission));
                        item.setProfit(item.getProfit().add(profit));

                        // External Payment
                        if (Objects.nonNull(order.getPaymentManagerToken()) && !order.getPaymentManagerToken().isEmpty()) {
                            if (Objects.nonNull(order.getPaymentTransactionId()) && !order.getPaymentTransactionId().isEmpty()) {
                                vo.getPayments().get("PAGSEGURO").setAmount(vo.getPayments().get("PAGSEGURO").getAmount().add(line.getAmount()));
                            } else {
                                vo.getPayments().get("CIELO").setAmount(vo.getPayments().get("CIELO").getAmount().add(line.getAmount()));
                            }
                        } else {
                            vo.getPayments().get("ENTERATIVEPAY").setAmount(vo.getPayments().get("ENTERATIVEPAY").getAmount().add(line.getAmount()));
                        }

                        vo.setAmount(vo.getAmount().add(line.getAmount()));
                        vo.setChartCommission(vo.getChartCommission().add(chartCommission));
                        vo.setProfit(vo.getProfit().add(profit));
                        vo.setQuantity(vo.getQuantity().add(BigDecimal.ONE));
                        vo.setAccountCommission(vo.getAccountCommission().add(accountCommission));
                    });
                });

                vo.getItems().put(k, item);
            });

            if (!searchForm.getRemoveEmpty() || vo.getAmount().signum() > 0) {
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
            return base.multiply(percentage.divide(new BigDecimal(100), RoundingMode.CEILING));
        } else {
            return BigDecimal.ZERO;
        }
    }
}
