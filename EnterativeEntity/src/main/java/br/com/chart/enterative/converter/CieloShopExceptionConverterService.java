package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.CieloShopException;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.CieloShopExceptionVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CieloShopExceptionConverterService extends ConverterService<CieloShopException, CieloShopExceptionVO> {

    public CieloShopExceptionConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public CieloShopException convert(CieloShopExceptionVO vo) {
        CieloShopException entity = new CieloShopException();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setShop(this.reflectionUtils.asHollowLink(Shop::new, vo.getShop()));
        return entity;
    }

    @Override
    public CieloShopExceptionVO convert(CieloShopException entity) {
        CieloShopExceptionVO vo = new CieloShopExceptionVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setShop(this.reflectionUtils.asNamedLink(ShopVO::new, entity.getShop()));
        return vo;
    }
}
