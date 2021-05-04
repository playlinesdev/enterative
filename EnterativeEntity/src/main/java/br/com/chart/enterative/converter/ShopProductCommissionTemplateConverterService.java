package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.ShopProductCommissionTemplate;
import br.com.chart.enterative.entity.ShopProductCommissionTemplateLine;
import br.com.chart.enterative.entity.Supplier;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ShopProductCommissionTemplateLineVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionTemplateVO;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateConverterService extends ConverterService<ShopProductCommissionTemplate, ShopProductCommissionTemplateVO> {

    @Autowired
    private ShopProductCommissionTemplateLineConverterService lineConverterService;

    public ShopProductCommissionTemplateConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShopProductCommissionTemplate convert(ShopProductCommissionTemplateVO vo) {
        ShopProductCommissionTemplate entity = new ShopProductCommissionTemplate();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setLines(vo.getLines().stream().map(l -> {
            ShopProductCommissionTemplateLine line = this.lineConverterService.convert(l);
            line.setTemplate(entity);
            return line;
        }).collect(Collectors.toList()));
        return entity;
    }

    @Override
    public ShopProductCommissionTemplateVO convert(ShopProductCommissionTemplate entity) {
        ShopProductCommissionTemplateVO vo = new ShopProductCommissionTemplateVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setLines(entity.getLines().stream().map(l -> {
            ShopProductCommissionTemplateLineVO line = this.lineConverterService.convert(l);
            return line;
        }).collect(Collectors.toList()));
        return vo;
    }
}