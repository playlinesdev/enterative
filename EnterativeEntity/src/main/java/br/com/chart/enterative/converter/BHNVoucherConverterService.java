package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.BHNVoucherVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNVoucherConverterService extends ConverterService<BHNVoucher, BHNVoucherVO> {

    public BHNVoucherConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public BHNVoucher convert(BHNVoucherVO vo) {
        BHNVoucher entity = new BHNVoucher();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCardNumber(vo.getCardNumber());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setEan(vo.getEan());
        entity.setId(vo.getId());
        entity.setPin(vo.getPin());
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        return entity;
    }

    @Override
    public BHNVoucherVO convert(BHNVoucher entity) {
        BHNVoucherVO vo = new BHNVoucherVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCardNumber(entity.getCardNumber());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setEan(entity.getEan());
        vo.setId(entity.getId());
        vo.setPin(entity.getPin());
        vo.setProduct(this.reflectionUtils.asHollowLink(ProductVO::new, entity.getProduct()));
        return vo;
    }

}
