package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.entity.vo.SDFDetailVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SDFDetailConverterService extends ConverterService<SDFDetail, SDFDetailVO> {

    public SDFDetailConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SDFDetail convert(SDFDetailVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDFDetailVO convert(SDFDetail entity) {
        SDFDetailVO vo = new SDFDetailVO();

        vo.setAcquiredTransactionDate(entity.getAcquiredTransactionDate());
        vo.setAcquiredTransactionTime(entity.getAcquiredTransactionTime());
        vo.setAcquirerId(entity.getAcquirerId());
        vo.setApprovalCode(entity.getApprovalCode());
        if (Objects.nonNull(entity.getBhnTransaction())) {
            vo.setBhnTransaction(this.reflectionUtils.createHollowLink(BHNTransactionVO::new, entity.getBhnTransaction()));
        }
        vo.setAuthResponseCode(entity.getAuthResponseCode());
        vo.setBhnTransactionId(entity.getBhnTransactionId());
        vo.setCardIssuerId(entity.getCardIssuerId());
        vo.setCardIssuerProcessorId(entity.getCardIssuerProcessorId());
        vo.setClerkId(entity.getClerkId());
        vo.setCommissionAmount(entity.getCommissionAmount());
        vo.setConsumerFeeAmount(entity.getConsumerFeeAmount());
        vo.setCurrencyCode(entity.getCurrencyCode());
        vo.setGiftCardNumber(entity.getGiftCardNumber());
        vo.setId(entity.getId());
        vo.setMerchantId(entity.getMerchantId());
        if (Objects.nonNull(entity.getMerchantName()) && entity.getMerchantName().length() > 15) {
            vo.setMerchantName(entity.getMerchantName().substring(0, 12) + "...");
        } else {
            vo.setMerchantName(entity.getMerchantName());
        }
        vo.setMerchantTransactionId(entity.getMerchantTransactionId());
        vo.setNetAmount(entity.getNetAmount());
        vo.setPosTransactionDate(entity.getPosTransactionDate());
        vo.setPosTransactionTime(entity.getPosTransactionTime());
        vo.setProductId(entity.getProductId());
        vo.setProductItemPrice(entity.getProductItemPrice());
        vo.setReversalTypeCode(entity.getReversalTypeCode());
        vo.setStatus(entity.getStatus());
        vo.setStoreId(entity.getStoreId());
        vo.setSystemTraceAuditNumber(entity.getSystemTraceAuditNumber());
        vo.setTerminalId(entity.getTerminalId());
        vo.setTotalTaxCommissionAmount(entity.getTotalTaxCommissionAmount());
        vo.setTotalTaxTransactionAmount(entity.getTotalTaxTransactionAmount());
        vo.setTransactionAmount(entity.getTransactionAmount());
        vo.setTransactionType(entity.getTransactionType());

        return vo;
    }

}
