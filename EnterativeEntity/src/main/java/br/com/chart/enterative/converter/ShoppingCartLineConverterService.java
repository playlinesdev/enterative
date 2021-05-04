package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.ShoppingCartLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ShoppingCartLineVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartLineConverterService extends ConverterService<ShoppingCartLine, ShoppingCartLineVO> {

    @Autowired
    private ProductConverterService productConverterService;

    public ShoppingCartLineConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShoppingCartLine convert(ShoppingCartLineVO vo) {
        ShoppingCartLine entity = new ShoppingCartLine();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        entity.setQuantity(vo.getQuantity());
        entity.setStoreStep(vo.getStoreStep());
        entity.setTotalAmount(vo.getTotalAmount());
        entity.setUserEmail(vo.getUserEmail());
        entity.setUserCellphone(vo.getUserCellphone());
        return entity;
    }

    @Override
    public ShoppingCartLineVO convert(ShoppingCartLine entity) {
        ShoppingCartLineVO vo = new ShoppingCartLineVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setProduct(this.productConverterService.convert(entity.getProduct()));
        vo.setQuantity(entity.getQuantity());
        vo.setStoreStep(entity.getStoreStep());
        vo.setTotalAmount(entity.getTotalAmount());
        vo.setUserEmail(entity.getUserEmail());
        vo.setUserCellphone(entity.getUserCellphone());
        return vo;
    }

}
