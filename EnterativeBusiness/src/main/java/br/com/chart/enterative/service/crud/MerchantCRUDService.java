package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.MerchantDAO;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.MerchantSearchVO;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class MerchantCRUDService extends UserAwareCRUDService<Merchant, Long, MerchantVO, MerchantSearchVO> {

    public MerchantCRUDService(UserAwareDAO<Merchant, Long> dao, ConverterService<Merchant, MerchantVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MerchantDAO dao() {
        return (MerchantDAO) super.dao();
    }

    public List<Merchant> findByStatusOrderByName(STATUS status) {
        return this.dao().findByStatusOrderByName(status);
    }

    public List<MerchantVO> findByStatusOrderByNameVO(STATUS status) {
        return this.findByStatusOrderByName(status).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    @Override
    public ServiceResponse validate(MerchantVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<Merchant> initEntitySupplier() {
        return Merchant::new;
    }

    @Override
    protected Supplier<MerchantVO> initVOSupplier() {
        return MerchantVO::new;
    }
}
