package br.com.chart.enterative.converter;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.dao.MerchantDAO;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.EpayVoucher;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLineEpay;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

/**
 * Responsavel por transformar uma solicitacao em uma Ativacao e um Voucher
 *
 * @author Cristhiano Roberto
 *
 */
@Service
public class WSRequestConverterService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private MerchantDAO merchantDAO;

    @Autowired
    private EnvParameterDAO parameterDAO;

    @Autowired
    private EnterativeReflectionUtils reflectionUtils;

    @Autowired
    private EnterativeUtils utils;

    public EpayActivation toEpayActivation(WSRequest request, String externalCode) {
        EpayActivation result = new EpayActivation();
        
        result.setExternalCode(externalCode);
        result.setCreatedAt(new Date());
        result.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, 0L));
        result.setStatus(ACTIVATION_STATUS.ACTIVATION);
        result.setTtlActivation(this.parameterDAO.get(ENVIRONMENT_PARAMETER.TTL_ATIVACAO));
        result.setTransactionId(UUID.randomUUID().toString());
        
        Merchant merchant = this.merchantDAO.findByMerchantIdentifier(request.getMerchant());
        result.setMerchant(merchant);
        
        result.setShopCode(request.getShop());
        result.setTerminal(request.getTerminal());
        
        result.setQueueStatus(ACTIVATION_QUEUE_STATUS.IDLE);
        result.setCallbackurl(request.getCallbackURL());
        result.setCallbackStatus(CALLBACK_STATUS.PENDING);
        return result;
    }
    
    /**
     * Adapta para uma Ativacao
     *
     * @param request
     * @param externalCode
     * @return
     */
    public BHNActivation toBHNActivation(WSRequest request, String externalCode) {
        BHNActivation retorno = new BHNActivation();

        retorno.setExternalCode(externalCode);
        retorno.setCreatedAt(new Date());
        retorno.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, 0L));
        retorno.setPriority(0);
        retorno.setStatus(ACTIVATION_STATUS.ACTIVATION);
        retorno.setTtlActivation(this.parameterDAO.get(ENVIRONMENT_PARAMETER.TTL_ATIVACAO));
        retorno.setTtlReversal(this.parameterDAO.get(ENVIRONMENT_PARAMETER.TTL_DESFAZIMENTO));

        Merchant merchant = merchantDAO.findByMerchantIdentifier(request.getMerchant());
        retorno.setMerchant(merchant);

        retorno.setShopCode(request.getShop());
        retorno.setTerminal(request.getTerminal());

        retorno.setQueueStatus(ACTIVATION_QUEUE_STATUS.IDLE);
        retorno.setCallbackurl(request.getCallbackURL());
        retorno.setCallbackStatus(CALLBACK_STATUS.PENDING);
        retorno.setType(request.getActivationType());

        return retorno;
    }

    public EpayVoucher toEpayVoucher(String barcode, WSRequestLineEpay epayLine) {
        EpayVoucher result = new EpayVoucher();
        
        result.setAmount(epayLine.getAmount());
        result.setAreaCode(epayLine.getAreaCode());
        result.setCreatedAt(new Date());
        result.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, 0L));
        
        String ean = barcode.substring(0, 12);
        result.setEan(ean);
        
        result.setEpayProductId(epayLine.getProductId());
        result.setOperator(epayLine.getOperator());
        result.setPhone(epayLine.getPhone());
        
        Product product = this.productDAO.findByEanStartingWith(ean);
        result.setProduct(product);
        
        return result;
    }
    
    /**
     * Adapta para um Voucher
     *
     * @param barcode
     * @return
     */
    public BHNVoucher toBHNVoucher(String barcode) {
        BHNVoucher retorno = new BHNVoucher();

        String upc = barcode.substring(0, 12);

        Product produto = productDAO.findByEanStartingWith(upc);
        String amountFormatado = String.format("%012.0f", (produto.getAmount().doubleValue() * 100));

        String cardNo = null;

        if (barcode.length() > 13) {
            cardNo = barcode.substring(12, 32);
            cardNo = this.utils.removeLeadingZeroes(cardNo);
        }

        retorno.setCreatedAt(new Date());
        retorno.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, 0L));
        retorno.setEan(upc);
        retorno.setAmount(amountFormatado);
        retorno.setCardNumber(cardNo);
        retorno.setProduct(produto);
        return retorno;
    }
}
