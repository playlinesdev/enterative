package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.ShopProductCommissionTemplateLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionTemplateLineVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateLineConverterService extends ConverterService<ShopProductCommissionTemplateLine, ShopProductCommissionTemplateLineVO> {

    public ShopProductCommissionTemplateLineConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShopProductCommissionTemplateLine convert(ShopProductCommissionTemplateLineVO vo) {
        ShopProductCommissionTemplateLine entity = new ShopProductCommissionTemplateLine();
        entity.setAmount(vo.getAmount());
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        if (Objects.nonNull(vo.getPercentage())) {
            entity.setPercentage(vo.getPercentage().divide(new BigDecimal(100)));
        }
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        return entity;
    }

    @Override
    public ShopProductCommissionTemplateLineVO convert(ShopProductCommissionTemplateLine entity) {
        ShopProductCommissionTemplateLineVO vo = new ShopProductCommissionTemplateLineVO();
        vo.setAmount(entity.getAmount());
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        if (Objects.nonNull(entity.getPercentage())) {
            vo.setPercentage(entity.getPercentage().multiply(new BigDecimal(100)));
        }
        vo.setProduct(this.reflectionUtils.asNamedLink(ProductVO::new, entity.getProduct()));
        return vo;
    }
}
