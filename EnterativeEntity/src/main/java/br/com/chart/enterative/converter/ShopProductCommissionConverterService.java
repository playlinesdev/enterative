package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionConverterService extends ConverterService<ShopProductCommission, ShopProductCommissionVO> {

    public ShopProductCommissionConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShopProductCommission convert(ShopProductCommissionVO vo) {
        ShopProductCommission entity = new ShopProductCommission();
        entity.setAmount(vo.getAmount());
        entity.setId(vo.getId());
        if (Objects.nonNull(vo.getPercentage())) {
            entity.setPercentage(vo.getPercentage().divide(new BigDecimal(100)));
        }
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        return entity;
    }

    @Override
    public ShopProductCommissionVO convert(ShopProductCommission entity) {
        ShopProductCommissionVO vo = new ShopProductCommissionVO();
        vo.setAmount(entity.getAmount());
        vo.setId(entity.getId());
        if (Objects.nonNull(entity.getPercentage())) {
            vo.setPercentage(entity.getPercentage().multiply(new BigDecimal(100)));
        }
        vo.setProduct(this.reflectionUtils.asNamedLink(ProductVO::new, entity.getProduct()));
        return vo;
    }
}
