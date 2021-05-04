package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.ShopPhone;
import br.com.chart.enterative.entity.vo.ShopPhoneVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopPhoneConverterService extends ConverterService<ShopPhone, ShopPhoneVO> {

    public ShopPhoneConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ShopPhone convert(ShopPhoneVO vo) {
        ShopPhone phone = new ShopPhone();
        phone.setContact(vo.getContact());
        if (Objects.nonNull(vo.getId())) {
            phone.setId(vo.getId());
        }
        phone.setPhone(vo.getPhone());
        phone.setType(vo.getType());
        return phone;
    }

    @Override
    public ShopPhoneVO convert(ShopPhone entity) {
        ShopPhoneVO vo = new ShopPhoneVO();
        vo.setContact(entity.getContact());
        vo.setId(entity.getId());
        vo.setPhone(entity.getPhone());
        vo.setType(entity.getType());
        return vo;
    }
}
