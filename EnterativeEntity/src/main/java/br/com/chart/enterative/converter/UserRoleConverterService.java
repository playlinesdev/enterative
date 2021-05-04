package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.UserRole;
import br.com.chart.enterative.entity.vo.UserRoleVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class UserRoleConverterService extends ConverterService<UserRole, UserRoleVO> {

    public UserRoleConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public UserRole convert(UserRoleVO vo) {
        UserRole entity = new UserRole();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        return entity;
    }

    @Override
    public UserRoleVO convert(UserRole entity) {
        UserRoleVO vo = new UserRoleVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setSelected(true);
        return vo;
    }
}
