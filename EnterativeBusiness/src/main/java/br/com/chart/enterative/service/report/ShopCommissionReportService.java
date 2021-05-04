package br.com.chart.enterative.service.report;

import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.ShopProductCommissionDAO;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.ShopCommissionReportLineVO;
import br.com.chart.enterative.vo.ShopCommissionReportVO;
import br.com.chart.enterative.vo.search.ShopCommissionReportSearchVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopCommissionReportService extends UserAwareComponent {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ShopProductCommissionDAO shopProductCommissionDAO;

    public List<ShopCommissionReportVO> assemble(ShopCommissionReportSearchVO searchForm) {
        List<ShopCommissionReportVO> result = new ArrayList<>();

        List<Product> products = this.productDAO.findAll(Sort.by("name"));

        if (Objects.nonNull(searchForm) && Objects.nonNull(searchForm.getProduct()) && Objects.nonNull(searchForm.getProduct().getId())) {
            products = products.stream().filter(p -> Objects.equals(p.getId(), searchForm.getProduct().getId())).collect(Collectors.toList());
        }

        products.stream().forEach(p -> {
            ShopCommissionReportVO vo = new ShopCommissionReportVO();
            if (Objects.nonNull(p.getCommissionPercentage())) {
                vo.setChartPercentage(p.getCommissionPercentage().multiply(new BigDecimal(100)));
            }
            vo.setChartValue(p.getCommissionAmount());
            vo.setFaceValue(p.getAmount());
            vo.setProductName(p.getName());
            vo.setLines(new ArrayList<>());

            List<ShopProductCommission> shopCommissions = this.shopProductCommissionDAO.findByProductId(p.getId());

            if (Objects.nonNull(searchForm) && Objects.nonNull(searchForm.getShop()) && Objects.nonNull(searchForm.getShop().getId())) {
                shopCommissions = shopCommissions.stream().filter(s -> Objects.equals(s.getShop().getId(), searchForm.getShop().getId())).collect(Collectors.toList());
            }

            shopCommissions.stream().forEach(c -> {
                ShopCommissionReportLineVO line = new ShopCommissionReportLineVO();
                line.setShopName(c.getShop().getName());
                line.setShopPercentage(c.getPercentage().multiply(new BigDecimal(100)));
                line.setShopValue(c.getAmount());
                vo.getLines().add(line);
            });

            result.add(vo);
        });

        return result;
    }
}
