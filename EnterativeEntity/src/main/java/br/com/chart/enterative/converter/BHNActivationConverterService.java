package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.BHNActivationVO;
import br.com.chart.enterative.entity.vo.BHNVoucherVO;
import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNActivationConverterService extends ConverterService<BHNActivation, BHNActivationVO> {

    public BHNActivationConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public BHNActivation convert(BHNActivationVO vo) {
        BHNActivation entity = new BHNActivation();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCallbackStatus(vo.getCallbackStatus());
        entity.setCallbackurl(vo.getCallbackurl());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setExternalCode(vo.getExternalCode());
        entity.setId(vo.getId());
        entity.setMerchant(this.reflectionUtils.asHollowLink(Merchant::new, vo.getMerchant()));
        entity.setPriority(vo.getPriority());
        entity.setQueueStatus(vo.getQueueStatus());
        entity.setResponseCode(vo.getResponseCode());
        entity.setReversalSentDate(vo.getReversalSentDate());
        entity.setShopCode(vo.getShopCode());
        entity.setStatus(vo.getStatus());
        entity.setTerminal(vo.getTerminal());
        entity.setTtlActivation(vo.getTtlActivation());
        entity.setTtlReversal(vo.getTtlReversal());
        entity.setType(vo.getType());
        entity.setVoucher(this.reflectionUtils.asHollowLink(BHNVoucher::new, vo.getVoucher()));
        return entity;
    }

    @Override
    public BHNActivationVO convert(BHNActivation entity) {
        BHNActivationVO vo = new BHNActivationVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCallbackStatus(entity.getCallbackStatus());
        vo.setCallbackurl(entity.getCallbackurl());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setExternalCode(entity.getExternalCode());
        vo.setId(entity.getId());
        vo.setMerchant(this.reflectionUtils.asNamedLink(MerchantVO::new, entity.getMerchant()));
        vo.setPriority(entity.getPriority());
        vo.setQueueStatus(entity.getQueueStatus());
        vo.setResponseCode(entity.getResponseCode());
        vo.setReversalSentDate(entity.getReversalSentDate());
        vo.setShopCode(entity.getShopCode());
        vo.setStatus(entity.getStatus());
        vo.setTerminal(entity.getTerminal());
        vo.setTtlActivation(entity.getTtlActivation());
        vo.setTtlReversal(entity.getTtlReversal());
        vo.setType(entity.getType());

        if (Objects.nonNull(entity.getVoucher()) && Objects.nonNull(entity.getVoucher().getName())) {
            vo.setVoucher(this.reflectionUtils.asNamedLink(BHNVoucherVO::new, entity.getVoucher()));
        } else {
            vo.setVoucher(this.reflectionUtils.asHollowLink(BHNVoucherVO::new, entity.getVoucher()));
        }
        return vo;
    }

}
