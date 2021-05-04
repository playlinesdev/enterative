package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Supplier;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SupplierConverterService extends ConverterService<Supplier, SupplierVO> {

    public SupplierConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Supplier convert(SupplierVO vo) {
        Supplier entity = new Supplier();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setSignature(vo.getSignature());
        entity.setSpecVersion(vo.getSpecVersion());
        return entity;
    }

    @Override
    public SupplierVO convert(Supplier entity) {
        SupplierVO vo = new SupplierVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setSignature(entity.getSignature());
        vo.setSpecVersion(entity.getSpecVersion());
        return vo;
    }

}
