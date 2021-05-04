package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.PagseguroPaymentMethod;
import br.com.chart.enterative.entity.vo.PagseguroPaymentMethodVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PagseguroPaymentMethodSearchVO;
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
public class PagseguroPaymentMethodCRUDService extends UserAwareCRUDService<PagseguroPaymentMethod, Long, PagseguroPaymentMethodVO, PagseguroPaymentMethodSearchVO> {

    public PagseguroPaymentMethodCRUDService(UserAwareDAO<PagseguroPaymentMethod, Long> dao, ConverterService<PagseguroPaymentMethod, PagseguroPaymentMethodVO> converter) {
        super(dao, converter);
    }

    public List<PagseguroPaymentMethodVO> findOrderByNameVO() {
        return this.findAllVOSorted(Comparator.comparing(PagseguroPaymentMethodVO::getName)).collect(Collectors.toList());
    }

    @Override
    protected Supplier<PagseguroPaymentMethod> initEntitySupplier() {
        return PagseguroPaymentMethod::new;
    }

    @Override
    protected Supplier<PagseguroPaymentMethodVO> initVOSupplier() {
        return PagseguroPaymentMethodVO::new;
    }

    @Override
    public ServiceResponse validate(PagseguroPaymentMethodVO vo, CRUD_OPERATION operation) {
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
