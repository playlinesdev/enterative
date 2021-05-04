package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.PurchaseOrderLineVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PurchaseOrderLineConverterService extends ConverterService<PurchaseOrderLine, PurchaseOrderLineVO> {

    public PurchaseOrderLineConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public PurchaseOrderLine convert(PurchaseOrderLineVO vo) {
        PurchaseOrderLine entity = new PurchaseOrderLine();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setStatus(vo.getStatus());
        return entity;
    }

    @Override
    public PurchaseOrderLineVO convert(PurchaseOrderLine entity) {
        PurchaseOrderLineVO vo = new PurchaseOrderLineVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setStatus(entity.getStatus());
        return vo;
    }

}
