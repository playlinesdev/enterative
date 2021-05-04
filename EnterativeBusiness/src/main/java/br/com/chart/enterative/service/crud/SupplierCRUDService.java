package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Supplier;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.SupplierSearchVO;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SupplierCRUDService extends UserAwareCRUDService<Supplier, Long, SupplierVO, SupplierSearchVO> {

    public SupplierCRUDService(UserAwareDAO<Supplier, Long> dao, ConverterService<Supplier, SupplierVO> converter) {
        super(dao, converter);
    }

    @Override
    protected java.util.function.Supplier<Supplier> initEntitySupplier() {
        return Supplier::new;
    }

    @Override
    protected java.util.function.Supplier<SupplierVO> initVOSupplier() {
        return SupplierVO::new;
    }

    @Override
    public ServiceResponse validate(SupplierVO vo, CRUD_OPERATION operation) {
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
