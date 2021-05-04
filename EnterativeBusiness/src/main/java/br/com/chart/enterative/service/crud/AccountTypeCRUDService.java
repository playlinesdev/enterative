package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTypeSearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTypeCRUDService extends UserAwareCRUDService<AccountType, Long, AccountTypeVO, AccountTypeSearchVO> {

    public AccountTypeCRUDService(UserAwareDAO<AccountType, Long> dao, ConverterService<AccountType, AccountTypeVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<AccountType> initEntitySupplier() {
        return AccountType::new;
    }

    @Override
    protected Supplier<AccountTypeVO> initVOSupplier() {
        return AccountTypeVO::new;
    }

    @Override
    public ServiceResponse validate(AccountTypeVO vo, CRUD_OPERATION operation) {
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
