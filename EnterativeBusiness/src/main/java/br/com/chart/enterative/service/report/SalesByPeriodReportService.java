package br.com.chart.enterative.service.report;

import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.REPORT_DATE_RANGE;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.repository.SaleOrderRepository;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.*;
import br.com.chart.enterative.vo.report.SalesByPeriodReportVO;
import br.com.chart.enterative.vo.report.SalesByProductItemReportVO;
import br.com.chart.enterative.vo.report.SalesByProductReportVO;
import br.com.chart.enterative.vo.search.SalesByPeriodSearchVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchColumnVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author William Leite
 */
@Service
public class SalesByPeriodReportService extends UserAwareComponent {

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    public SalesByPeriodSearchVO initSearchVO() {
        return new SalesByPeriodSearchVO();
    }

    @Transactional
    public Map<String, Object> assembleReportVariables(SalesByPeriodSearchVO searchForm) {
        if (Objects.isNull(searchForm.getStartDate())) {
            searchForm.setStartDate(this.utils.fromLocalDate(this.utils.toLocalDate(new Date()).minusYears(1L)));
        }
        if (Objects.isNull(searchForm.getEndDate())) {
            searchForm.setEndDate(new Date());
        }

        Map<String, Object> result = new HashMap<>();

        List<Shop> shops = this.shopService.findAll(Sort.by("name"));
        List<SalesByPeriodReportVO> list = new ArrayList<>();

        shops.forEach(shop -> {
            SalesByPeriodReportVO vo = new SalesByPeriodReportVO();
            vo.setName(shop.getName());
            vo.setState(shop.getState());
            vo.setCity(shop.getCity());
            vo.setDistrict(shop.getDistrict());

            BigDecimal amount = this.saleOrderRepository.sumByShopAndStatus(shop.getId(), SALE_ORDER_STATUS.ACTIVATED, searchForm.getStartDate(), searchForm.getEndDate());
            vo.setAmount(Optional.ofNullable(amount).orElse(BigDecimal.ZERO));
            list.add(vo);
        });

        if (searchForm.getRemoveEmpty()) {
            list.removeIf(v -> v.getAmount().signum() <= 0);
        }

        result.put("objectList", list.toArray());
        return result;
    }
}