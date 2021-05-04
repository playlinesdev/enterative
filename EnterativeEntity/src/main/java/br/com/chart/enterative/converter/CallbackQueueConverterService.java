package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.CallbackQueue;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.vo.BHNActivationVO;
import br.com.chart.enterative.entity.vo.CallbackQueueVO;
import br.com.chart.enterative.entity.vo.EpayActivationVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CallbackQueueConverterService extends ConverterService<CallbackQueue, CallbackQueueVO> {

    public CallbackQueueConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public CallbackQueue convert(CallbackQueueVO vo) {
        CallbackQueue queue = new CallbackQueue();
        queue.setBhnActivation(this.reflectionUtils.asHollowLink(BHNActivation::new, vo.getBhnActivation()));
        queue.setEpayActivation(this.reflectionUtils.asHollowLink(EpayActivation::new, vo.getEpayActivation()));
        return queue;
    }

    @Override
    public CallbackQueueVO convert(CallbackQueue entity) {
        CallbackQueueVO vo = new CallbackQueueVO();
        vo.setBhnActivation(this.reflectionUtils.asHollowLink(BHNActivationVO::new, entity.getBhnActivation()));
        vo.setEpayActivation(this.reflectionUtils.asHollowLink(EpayActivationVO::new, entity.getEpayActivation()));
        return vo;
    }
}