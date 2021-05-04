package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.UserRole;
import br.com.chart.enterative.entity.vo.UserRoleVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.USER_ROLE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.UserRoleSearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class UserRoleCRUDService extends UserAwareCRUDService<UserRole, Long, UserRoleVO, UserRoleSearchVO> {

    public UserRoleCRUDService(UserAwareDAO<UserRole, Long> dao, ConverterService<UserRole, UserRoleVO> converter) {
        super(dao, converter);
    }

    public UserRole findClientRole() {
        UserRole role = this.dao().findByName(USER_ROLE.ROLE_CUSTOMER.getFullRole());
        return role;
    }

    public UserRole findBalanceRole() {
        UserRole role = this.dao().findByName(USER_ROLE.ROLE_ENTERATIVE_BALANCE.getFullRole());
        return role;
    }

    @Override
    protected Supplier<UserRole> initEntitySupplier() {
        return UserRole::new;
    }

    @Override
    protected Supplier<UserRoleVO> initVOSupplier() {
        return UserRoleVO::new;
    }

    @Override
    public ServiceResponse validate(UserRoleVO vo, CRUD_OPERATION operation) {
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
