package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ServerConverterService extends ConverterService<Server, ServerVO> {

    public ServerConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Server convert(ServerVO vo) {
        Server entity = new Server();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setEchoProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getEchoProduct()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setSequence(vo.getSequence());
        entity.setStatus(vo.getStatus());
        entity.setActivationProcess(vo.getActivationProcess());
        return entity;
    }

    @Override
    public ServerVO convert(Server entity) {
        ServerVO vo = new ServerVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setEchoProduct(this.reflectionUtils.asNamedLink(ProductVO::new, entity.getEchoProduct()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setSequence(entity.getSequence());
        vo.setStatus(entity.getStatus());
        vo.setActivationProcess(entity.getActivationProcess());
        return vo;
    }

}
