package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.ProductText;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductTextConverterService extends ConverterService<ProductText, ProductTextVO> {

    public ProductTextConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ProductText convert(ProductTextVO vo) {
        ProductText entity = new ProductText();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setDescription(vo.getDescription());
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setType(vo.getType());
        return entity;
    }

    @Override
    public ProductTextVO convert(ProductText entity) {
        ProductTextVO vo = new ProductTextVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setDescription(entity.getDescription());
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setType(entity.getType());
        return vo;
    }

}
