package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ResourceVO;
import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ResourceConverterService extends ConverterService<Resource, ResourceVO> {

    public ResourceConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Resource convert(ResourceVO vo) {
        Resource entity = new Resource();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setServer(this.reflectionUtils.asHollowLink(Server::new, vo.getServer()));
        entity.setType(vo.getType());
        entity.setUrl(vo.getUrl());
        return entity;
    }

    @Override
    public ResourceVO convert(Resource entity) {
        ResourceVO vo = new ResourceVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setServer(this.reflectionUtils.asNamedLink(ServerVO::new, entity.getServer()));
        vo.setType(entity.getType());
        vo.setUrl(entity.getUrl());
        return vo;
    }

}
