package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.ShoppingCart;
import br.com.chart.enterative.entity.ShoppingCartLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ShoppingCartLineVO;
import br.com.chart.enterative.entity.vo.ShoppingCartVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartConverterService extends ConverterService<ShoppingCart, ShoppingCartVO> {

    @Autowired
    private ShoppingCartLineConverterService lineConverterService;

    public ShoppingCartConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShoppingCart convert(ShoppingCartVO vo) {
        ShoppingCart entity = new ShoppingCart();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setLines(vo.getLines().stream().map(l -> {
            ShoppingCartLine line = this.lineConverterService.convert(l);
            line.setCart(entity);
            return line;
        }).collect(Collectors.toList()));
        return entity;
    }

    @Override
    public ShoppingCartVO convert(ShoppingCart entity) {
        ShoppingCartVO vo = new ShoppingCartVO();
        if(Objects.nonNull(entity)){
            vo.setAlteredAt(entity.getAlteredAt());
            vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
            vo.setAmount(entity.getAmount());
            vo.setCreatedAt(entity.getCreatedAt());
            vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
            vo.setId(entity.getId());
            vo.setLines(entity.getLines().stream().map(this.lineConverterService::convert).collect(Collectors.toList()));
        }else{
            vo.setLines(new LinkedList<ShoppingCartLineVO>());
        }
        return vo;
    }
}
