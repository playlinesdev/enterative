package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductCategory;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductCategorySearchVO;
import java.util.ArrayList;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductCategoryCRUDService extends UserAwareCRUDService<ProductCategory, Long, ProductCategoryVO, ProductCategorySearchVO> {

    public ProductCategoryCRUDService(UserAwareDAO<ProductCategory, Long> dao, ConverterService<ProductCategory, ProductCategoryVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<ProductCategory> initEntitySupplier() {
        return () -> {
            ProductCategory category = new ProductCategory();
            category.setChildren(new ArrayList<>());
            return category;
        };
    }

    @Override
    protected Supplier<ProductCategoryVO> initVOSupplier() {
        return () -> {
            ProductCategoryVO vo = new ProductCategoryVO();
            vo.setChildren(new ArrayList<>());
            return vo;
        };
    }

    @Override
    public ServiceResponse validate(ProductCategoryVO vo, CRUD_OPERATION operation) {
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

}
