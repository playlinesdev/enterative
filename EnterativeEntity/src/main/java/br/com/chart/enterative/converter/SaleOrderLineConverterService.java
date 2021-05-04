package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.SaleOrderLineVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.Objects;

import br.com.chart.enterative.helper.EnterativeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SaleOrderLineConverterService extends ConverterService<SaleOrderLine, SaleOrderLineVO> {

    @Autowired
    private ProductTextConverterService productTextConverterService;

    @Autowired
    private EnterativeUtils utils;

    public SaleOrderLineConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SaleOrderLine convert(SaleOrderLineVO vo) {
        SaleOrderLine entity = new SaleOrderLine();
        entity.setActivationStatus(vo.getActivationStatus());
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setBarcode(vo.getBarcode());
        entity.setCallbackStatus(vo.getCallbackStatus());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setExternalCode(vo.getExternalCode());
        entity.setId(vo.getId());
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        entity.setResponse(vo.getResponse());
        entity.setResponseAux(vo.getResponseAux());
        entity.setReturnDate(vo.getReturnDate());
        entity.setStatus(vo.getStatus());
        
        entity.setUserEmail(vo.getUserEmail());
        entity.setUserEmailStatus(vo.getUserEmailStatus());
        
        entity.setUserCellphone(vo.getUserCellphone());
        entity.setSmsStatus(vo.getSmsStatus());
        
        entity.setAreaCode(vo.getAreaCode());
        entity.setCatalogId(vo.getCatalogId());
        entity.setOperator(vo.getOperator());
        entity.setPhone(vo.getPhone());
        return entity;
    }

    @Override
    public SaleOrderLineVO convert(SaleOrderLine entity) {
        SaleOrderLineVO vo = new SaleOrderLineVO();
        vo.setActivationStatus(entity.getActivationStatus());
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setBarcode(entity.getBarcode());
        vo.setCallbackStatus(entity.getCallbackStatus());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setExternalCode(entity.getExternalCode());
        vo.setId(entity.getId());
        vo.setProduct(this.reflectionUtils.asNamedLink(ProductVO::new, entity.getProduct()));
        vo.getProduct().setImagem(this.utils.retrieveImagem(entity.getProduct().getId()));
        if (Objects.nonNull(entity.getProduct().getTermsAndConditions())) {
            vo.getProduct().setTermsAndConditions(this.productTextConverterService.convert(entity.getProduct().getTermsAndConditions()));
        }
        if (Objects.nonNull(entity.getProduct().getActivationInstructions())) {
            vo.getProduct().setActivationInstructions(this.productTextConverterService.convert(entity.getProduct().getActivationInstructions()));
        }
        vo.setResponse(entity.getResponse());
        vo.setResponseAux(entity.getResponseAux());
        vo.setReturnDate(entity.getReturnDate());
        vo.setStatus(entity.getStatus());
        
        vo.setUserEmail(entity.getUserEmail());
        vo.setUserEmailStatus(entity.getUserEmailStatus());
        
        vo.setUserCellphone(entity.getUserCellphone());
        vo.setSmsStatus(entity.getSmsStatus());
        
        vo.setAreaCode(entity.getAreaCode());
        vo.setCatalogId(entity.getCatalogId());
        vo.setOperator(entity.getOperator());
        vo.setPhone(entity.getPhone());
        return vo;
    }

}
