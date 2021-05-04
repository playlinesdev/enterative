package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopProductCommissionDAO;
import br.com.chart.enterative.dao.ShopProductCommissionTemplateLineDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionTemplateLineVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShopProductCommissionSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateLineCRUDService extends UserAwareCRUDService<ShopProductCommissionTemplateLine, Long, ShopProductCommissionTemplateLineVO, ShopProductCommissionSearchVO> {

    public ShopProductCommissionTemplateLineCRUDService(UserAwareDAO<ShopProductCommissionTemplateLine, Long> dao, ConverterService<ShopProductCommissionTemplateLine, ShopProductCommissionTemplateLineVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionTemplateLineDAO dao() {
        return (ShopProductCommissionTemplateLineDAO) super.dao();
    }

    public ShopProductCommissionTemplateLineVO initVO(ProductVO product) {
        ShopProductCommissionTemplateLineVO vo = this.initVO();
        vo.setProduct(product);
        return vo;
    }

    @Override
    public ServiceResponse validate(ShopProductCommissionTemplateLineVO vo, CRUD_OPERATION operation) {
        ServiceResponse response = new ServiceResponse();
        switch (operation) {
            case CREATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case DELETE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case READ:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case UPDATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
        }
        return response;
    }

    @Override
    protected Supplier<ShopProductCommissionTemplateLine> initEntitySupplier() {
        return ShopProductCommissionTemplateLine::new;
    }

    @Override
    protected Supplier<ShopProductCommissionTemplateLineVO> initVOSupplier() {
        return ShopProductCommissionTemplateLineVO::new;
    }

}
