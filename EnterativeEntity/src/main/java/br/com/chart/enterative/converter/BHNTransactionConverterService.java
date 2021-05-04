package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.entity.vo.BHNActivationVO;
import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.entity.vo.ResourceVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.helper.EnterativeUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNTransactionConverterService extends ConverterService<BHNTransaction, BHNTransactionVO> {

    @Autowired
    private BHNActivationConverterService activationConverterService;

    @Autowired
    private EnterativeUtils utils;

    public BHNTransactionConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public BHNTransaction convert(BHNTransactionVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BHNTransactionVO convert(BHNTransaction entity) {
        BHNTransactionVO vo = new BHNTransactionVO();

        vo.setId(entity.getId());

        try {
            Date tdate = this.toDate("yyMMdd", entity.getLocalTransactionDate());
            vo.setLocalTransactionDate(this.fromDate("dd/MM/yyyy", tdate));

            Date ttime = this.toDate("HHmmss", entity.getLocalTransactionTime());
            vo.setLocalTransactionTime(this.fromDate("HH:mm:ss", ttime));

            vo.setTransactionReturnDate(entity.getTransactionReturnDate());

            Date tdatetime = this.toDate("yyMMddHHmmss", entity.getTransmissionDateTime());
            vo.setTransmissionDateTime(this.fromDate("dd/MM/yyyy HH:mm:ss", tdatetime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        //
        vo.setActivationAccountNumber(entity.getActivationAccountNumber());
        vo.setAuthIdentificationResponse(entity.getAuthIdentificationResponse());
        vo.setBhnActivation(this.reflectionUtils.asHollowLink(BHNActivationVO::new, entity.getBhnActivation()));
        vo.setCorrelatedTransactionUniqueId(entity.getCorrelatedTransactionUniqueId());
        vo.setTransactionCurrencyCode(entity.getTransactionCurrencyCode());
        vo.setMerchantTerminalId(entity.getMerchantTerminalId());
        vo.setNetworkManagementCode(entity.getNetworkManagementCode());
        vo.setPrimaryAccountNumber(entity.getPrimaryAccountNumber());
        vo.setProductCategoryCode(entity.getProductCategoryCode());
        vo.setProductId(entity.getProductId());
        vo.setRedemptionAccountNumber(entity.getRedemptionAccountNumber());
        vo.setRedemptionPin(entity.getRedemptionPin());
        vo.setResponseCode(entity.getResponseCode());
        vo.setStatusCode(entity.getStatusCode());
        vo.setSystemTraceAuditNumber(entity.getSystemTraceAuditNumber());
        vo.setTransactionAmount(entity.getTransactionAmount());
        vo.setTransactionUniqueId(entity.getTransactionUniqueId());
        vo.setDirection(entity.getDirection());
        if (Objects.nonNull(entity.getResource()) && Objects.nonNull(entity.getResource().getName())) {
            vo.setResource(this.reflectionUtils.asNamedLink(ResourceVO::new, entity.getResource()));
        } else if (Objects.nonNull(entity.getResource())) {
            entity.getResource().setName(String.format("%s - %s", entity.getResource().getServer().getName(), entity.getResource().getType().getDescription()));
        }

        return vo;
    }

    public BHNTransactionVO convert(BHNTransaction bhnTransaction, SDFDetail detail) {
        BHNTransactionVO vo = this.convert(bhnTransaction);
        vo.setDiscrepancies(this.retrieveDescrepancies(bhnTransaction, detail));
        return vo;
    }

    public List<String> retrieveDescrepancies(BHNTransaction transaction, SDFDetail detail) {
        List<String> discrepancies = new ArrayList<>();

        if (!Objects.equals(transaction.getLocalTransactionDate(), this.fromDate("yyMMdd", detail.getPosTransactionDate()))) {
            discrepancies.add("localTransactionDate");
        }
        if (!Objects.equals(transaction.getLocalTransactionTime(), this.fromDate("HHmmss", detail.getPosTransactionTime()))) {
            discrepancies.add("localTransactionTime");
        }
        if (!Objects.equals(transaction.getMerchantTerminalId(), this.utils.retrieveMerchantTerminalID(detail.getStoreId(), detail.getTerminalId()))) {
            discrepancies.add("merchantTerminalId");
        }
        if (!Objects.equals(transaction.getPrimaryAccountNumber(), detail.getGiftCardNumber())) {
            discrepancies.add("primaryAccountNumber");
        }
        if (!Objects.equals(transaction.getActivationAccountNumber(), detail.getGiftCardNumber())) {
            discrepancies.add("activationAccountNumber");
        }
        if (!Objects.equals(transaction.getProductId(), detail.getProductId())) {
            discrepancies.add("productId");
        }
        if (!Objects.equals(transaction.getResponseCode(), detail.getAuthResponseCode())) {
            discrepancies.add("responseCode");
        }
        if (!Objects.equals(transaction.getSystemTraceAuditNumber(), detail.getSystemTraceAuditNumber())) {
            discrepancies.add("systemTraceAuditNumber");
        }
        double originalAmount = this.fromDotlessString(transaction.getTransactionAmount(), 2).doubleValue();
        double detailAmount = detail.getProductItemPrice().doubleValue();
        if (!Objects.equals(originalAmount, detailAmount)) {
            discrepancies.add("transactionAmount");
        }

        return discrepancies;
    }

}
