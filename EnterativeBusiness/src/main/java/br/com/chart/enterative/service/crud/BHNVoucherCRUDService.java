package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.vo.BHNVoucherVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.BHNVoucherSearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNVoucherCRUDService extends UserAwareCRUDService<BHNVoucher, Long, BHNVoucherVO, BHNVoucherSearchVO> {

    public BHNVoucherCRUDService(UserAwareDAO<BHNVoucher, Long> dao, ConverterService<BHNVoucher, BHNVoucherVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<BHNVoucher> initEntitySupplier() {
        return BHNVoucher::new;
    }

    @Override
    protected Supplier<BHNVoucherVO> initVOSupplier() {
        return BHNVoucherVO::new;
    }

    @Override
    public ServiceResponse validate(BHNVoucherVO vo, CRUD_OPERATION operation) {
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
