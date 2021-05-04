package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ProductTextDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductText;
import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductTextSearchVO;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductTextCRUDService extends UserAwareCRUDService<ProductText, Long, ProductTextVO, ProductTextSearchVO> {

    public ProductTextCRUDService(UserAwareDAO<ProductText, Long> dao, ConverterService<ProductText, ProductTextVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductTextDAO dao() {
        return (ProductTextDAO) super.dao();
    }

    @Override
    protected Supplier<ProductText> initEntitySupplier() {
        return ProductText::new;
    }

    @Override
    protected Supplier<ProductTextVO> initVOSupplier() {
        return ProductTextVO::new;
    }

    @Override
    public ServiceResponse validate(ProductTextVO vo, CRUD_OPERATION operation) {
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

    public List<ProductTextVO> findByType(PRODUCT_TEXT_TYPE type) {
        return this.dao().findByType(type).stream().map(this.converter()::convert).sorted(Comparator.comparing(ProductTextVO::getName)).collect(Collectors.toList());
    }

}
