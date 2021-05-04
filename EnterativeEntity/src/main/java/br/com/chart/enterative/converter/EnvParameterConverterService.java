package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.EnvParameter;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.EnvParameterVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EnvParameterConverterService extends ConverterService<EnvParameter, EnvParameterVO> {

    public EnvParameterConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public EnvParameter convert(EnvParameterVO vo) {
        EnvParameter entity = new EnvParameter();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setParam(vo.getParam());
        entity.setValue(vo.getValue());
        return entity;
    }

    @Override
    public EnvParameterVO convert(EnvParameter entity) {
        EnvParameterVO vo = new EnvParameterVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setParam(entity.getParam());
        vo.setValue(entity.getValue());
        return vo;
    }
}
