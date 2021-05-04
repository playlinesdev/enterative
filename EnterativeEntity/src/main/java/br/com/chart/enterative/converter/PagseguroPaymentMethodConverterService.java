package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.PagseguroPaymentMethod;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.PagseguroPaymentMethodVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PagseguroPaymentMethodConverterService extends ConverterService<PagseguroPaymentMethod, PagseguroPaymentMethodVO> {

    public PagseguroPaymentMethodConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public PagseguroPaymentMethod convert(PagseguroPaymentMethodVO vo) {
        PagseguroPaymentMethod entity = new PagseguroPaymentMethod();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));        
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setPaymentMethod(vo.getPaymentMethod());
        entity.setStatus(vo.getStatus());
        return entity;
    }

    @Override
    public PagseguroPaymentMethodVO convert(PagseguroPaymentMethod entity) {
        PagseguroPaymentMethodVO vo = new PagseguroPaymentMethodVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));        
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setPaymentMethod(entity.getPaymentMethod());
        vo.setStatus(entity.getStatus());
        return vo;
    }

}
