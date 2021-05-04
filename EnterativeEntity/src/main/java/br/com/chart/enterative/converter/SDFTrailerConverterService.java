package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.SDFTrailer;
import br.com.chart.enterative.entity.vo.SDFTrailerVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SDFTrailerConverterService extends ConverterService<SDFTrailer, SDFTrailerVO> {

    public SDFTrailerConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SDFTrailer convert(SDFTrailerVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDFTrailerVO convert(SDFTrailer entity) {
        SDFTrailerVO vo = new SDFTrailerVO();

        vo.setFileTransmissionDate(entity.getFileTransmissionDate());
        vo.setFiller(entity.getFiller());
        vo.setId(entity.getId());
        vo.setNetTransactionAmount(entity.getNetTransactionAmount());
        vo.setTotalActivationAmount(entity.getTotalActivationAmount());
        vo.setTotalActivationCount(entity.getTotalActivationCount());
        vo.setTotalCommissionAmount(entity.getTotalCommissionAmount());
        vo.setTotalConsumerFeeAmount(entity.getTotalConsumerFeeAmount());
        vo.setTotalRedemptionAmount(entity.getTotalRedemptionAmount());
        vo.setTotalRedemptionCount(entity.getTotalRedemptionCount());
        vo.setTotalRefundActivationAmount(entity.getTotalRefundActivationAmount());
        vo.setTotalRefundActivationCount(entity.getTotalRefundActivationCount());
        vo.setTotalReloadAmount(entity.getTotalReloadAmount());
        vo.setTotalReloadCount(entity.getTotalReloadCount());
        vo.setTotalReturnAmount(entity.getTotalReturnAmount());
        vo.setTotalReturnCount(entity.getTotalReturnCount());
        vo.setTotalReversalAmount(entity.getTotalReversalAmount());
        vo.setTotalReversalCount(entity.getTotalReversalCount());
        vo.setTotalTaxCommissionAmount(entity.getTotalTaxCommissionAmount());
        vo.setTotalTaxTransactionAmount(entity.getTotalTaxTransactionAmount());
        vo.setTotalTransactionCount(entity.getTotalTransactionCount());

        return vo;
    }

}
