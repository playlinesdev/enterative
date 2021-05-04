package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductHighlight;
import br.com.chart.enterative.entity.vo.ProductHighlightVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductHighlightSearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductHighlightCRUDService extends UserAwareCRUDService<ProductHighlight, Long, ProductHighlightVO, ProductHighlightSearchVO> {

    public ProductHighlightCRUDService(UserAwareDAO<ProductHighlight, Long> dao, ConverterService<ProductHighlight, ProductHighlightVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<ProductHighlight> initEntitySupplier() {
        return ProductHighlight::new;
    }

    @Override
    protected Supplier<ProductHighlightVO> initVOSupplier() {
        return ProductHighlightVO::new;
    }

    @Override
    public ServiceResponse validate(ProductHighlightVO vo, CRUD_OPERATION operation) {
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
