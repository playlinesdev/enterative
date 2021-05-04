package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.FileUploadDAO;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.chart.enterative.helper.EnterativeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductConverterService extends ConverterService<Product, ProductVO> {

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private ProductDAO productDAO;

    public ProductConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Product convert(ProductVO vo) {
        Product entity;

        if (Objects.isNull(vo.getId())) {
            entity = new Product();
            entity.setCreatedAt(new Date());
        } else {
            entity = this.productDAO.findOne(vo.getId());
        }

        entity.setActivationFee(vo.getActivationFee());
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCategory(this.reflectionUtils.asHollowLink(ProductCategory::new, vo.getCategory()));
        if (Objects.nonNull(vo.getCommissionPercentage())) {
            entity.setCommissionPercentage(vo.getCommissionPercentage().divide(new BigDecimal(100)));
        }
        entity.setCommissionAmount(vo.getCommissionAmount());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setDisplayName(cleanTextEdges(vo.getDisplayName()));
        entity.setEan(cleanTextEdges(vo.getEan()));
        entity.setName(cleanTextEdges(vo.getName()));
        entity.setStatus(vo.getStatus());
        entity.setSupplier(this.reflectionUtils.asHollowLink(Supplier::new, vo.getSupplier()));
        entity.setSupplierCode(cleanTextEdges(vo.getSupplierCode()));
        entity.setSupplierRefCategory(cleanTextEdges(vo.getSupplierRefCategory()));
        entity.setSupplierRefId(cleanTextEdges(vo.getSupplierRefId()));
        entity.setSupplierRefType(cleanTextEdges(vo.getSupplierRefType()));
        entity.setType(vo.getType());
        entity.setTermsAndConditions(this.reflectionUtils.asHollowLink(ProductText::new, vo.getTermsAndConditions()));
        entity.setActivationInstructions(this.reflectionUtils.asHollowLink(ProductText::new, vo.getActivationInstructions()));
        entity.setActivationProcess(vo.getActivationProcess());
        entity.setPrimaryAccountNumber(cleanTextEdges(vo.getPrimaryAccountNumber()));
        entity.setSendsSMS(vo.getSendsSMS());
        entity.setSmsTemplate(cleanTextEdges(vo.getSmsTemplate()));
        return entity;
    }

    public ProductVO convertWithoutImage(Product entity) {
        ProductVO vo = new ProductVO();
        vo.setActivationFee(entity.getActivationFee());
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCategory(this.reflectionUtils.asHollowLink(ProductCategoryVO::new, entity.getCategory()));
        if (Objects.nonNull(entity.getCommissionPercentage())) {
            vo.setCommissionPercentage(entity.getCommissionPercentage().multiply(new BigDecimal(100)));
        }
        vo.setCommissionAmount(entity.getCommissionAmount());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setDisplayName(entity.getDisplayName());
        vo.setEan(entity.getEan());
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setStatus(entity.getStatus());
        vo.setSupplier(this.reflectionUtils.asHollowLink(SupplierVO::new, entity.getSupplier()));
        vo.setSupplierCode(entity.getSupplierCode());
        vo.setSupplierRefCategory(entity.getSupplierRefCategory());
        vo.setSupplierRefId(entity.getSupplierRefId());
        vo.setSupplierRefType(entity.getSupplierRefType());
        vo.setType(entity.getType());
        vo.setTermsAndConditions(this.reflectionUtils.asNamedLink(ProductTextVO::new, entity.getTermsAndConditions()));
        vo.setActivationInstructions(this.reflectionUtils.asNamedLink(ProductTextVO::new, entity.getActivationInstructions()));
        vo.setActivationProcess(entity.getActivationProcess());
        vo.setPrimaryAccountNumber(entity.getPrimaryAccountNumber());
        vo.setSendsSMS(entity.getSendsSMS());
        vo.setSmsTemplate(entity.getSmsTemplate());
        return vo;
    }

    @Override
    public ProductVO convert(Product entity) {
        ProductVO vo = new ProductVO();
        vo.setActivationFee(entity.getActivationFee());
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCategory(this.reflectionUtils.asHollowLink(ProductCategoryVO::new, entity.getCategory()));
        if (Objects.nonNull(entity.getCommissionPercentage())) {
            vo.setCommissionPercentage(entity.getCommissionPercentage().multiply(new BigDecimal(100)));
        }
        vo.setCommissionAmount(entity.getCommissionAmount());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setDisplayName(entity.getDisplayName());
        vo.setEan(entity.getEan());
        vo.setId(entity.getId());

        vo.setImagem(this.utils.retrieveImagem(entity.getId()));

        vo.setName(entity.getName());
        vo.setStatus(entity.getStatus());
        vo.setSupplier(this.reflectionUtils.asHollowLink(SupplierVO::new, entity.getSupplier()));
        vo.setSupplierCode(entity.getSupplierCode());
        vo.setSupplierRefCategory(entity.getSupplierRefCategory());
        vo.setSupplierRefId(entity.getSupplierRefId());
        vo.setSupplierRefType(entity.getSupplierRefType());
        vo.setType(entity.getType());
        vo.setTermsAndConditions(this.reflectionUtils.asNamedLink(ProductTextVO::new, entity.getTermsAndConditions()));
        vo.setActivationInstructions(this.reflectionUtils.asNamedLink(ProductTextVO::new, entity.getActivationInstructions()));
        vo.setActivationProcess(entity.getActivationProcess());
        vo.setPrimaryAccountNumber(entity.getPrimaryAccountNumber());
        vo.setSendsSMS(entity.getSendsSMS());
        vo.setSmsTemplate(entity.getSmsTemplate());
        return vo;
    }



}
