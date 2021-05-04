package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransactionCategory;
import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTransactionCategorySearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William
 */
@Service
public class AccountTransactionCategoryCRUDService extends UserAwareCRUDService<AccountTransactionCategory, Long, AccountTransactionCategoryVO, AccountTransactionCategorySearchVO> {

    public AccountTransactionCategoryCRUDService(UserAwareDAO<AccountTransactionCategory, Long> dao, ConverterService<AccountTransactionCategory, AccountTransactionCategoryVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<AccountTransactionCategory> initEntitySupplier() {
        return AccountTransactionCategory::new;
    }

    @Override
    protected Supplier<AccountTransactionCategoryVO> initVOSupplier() {
        return AccountTransactionCategoryVO::new;
    }

    @Override
    public ServiceResponse validate(AccountTransactionCategoryVO vo, CRUD_OPERATION operation) {
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
