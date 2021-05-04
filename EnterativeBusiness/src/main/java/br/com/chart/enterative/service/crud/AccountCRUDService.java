package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.AccountDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountType;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountSearchVO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountCRUDService extends UserAwareCRUDService<Account, Long, AccountVO, AccountSearchVO> {

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountTypeCRUDService accountTypeService;

    public AccountCRUDService(UserAwareDAO<Account, Long> dao, ConverterService<Account, AccountVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountDAO dao() {
        return (AccountDAO) super.dao();
    }

    public List<AccountVO> findByStatusOrderByName(STATUS status) {
        return this.dao().findByStatusOrderByName(status).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public Account create(User user) {
        Account account = this.initEntity();
        account.setStatus(STATUS.ACTIVE);
        account.setName(user.getName());

        account.setUsers(new ArrayList<>());
        account.getUsers().add(user);

        Long accountTypeID = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_ACCOUNT_TYPE_USER);
        AccountType type = this.accountTypeService.findOne(accountTypeID);
        account.setType(type);

        return this.dao().saveAndFlush(account, this.systemUserId());
    }

    public PageWrapper<AccountVO> retrieveAccounts(AccountSearchVO searchForm, Pageable pageable, String url) {
        Page<Account> page;
        if (Objects.nonNull(searchForm.getName())) {
            if (Objects.nonNull(searchForm.getStatus())) {
                if (Objects.nonNull(searchForm.getType())) {
                    page = this.dao().findByNameIgnoreCaseContainingAndStatusAndTypeIdOrderByName(searchForm.getName(), searchForm.getStatus(), searchForm.getType().getId(), pageable);
                } else {
                    page = this.dao().findByNameIgnoreCaseContainingAndStatusOrderByName(searchForm.getName(), searchForm.getStatus(), pageable);
                }
            } else {
                if (Objects.nonNull(searchForm.getType())) {
                    page = this.dao().findByNameIgnoreCaseContainingAndTypeIdOrderByName(searchForm.getName(), searchForm.getType().getId(), pageable);
                } else {
                    page = this.dao().findByNameIgnoreCaseContainingOrderByName(searchForm.getName(), pageable);
                }
            }
        } else {
            if (Objects.nonNull(searchForm.getStatus())) {
                if (Objects.nonNull(searchForm.getType())) {
                    page = this.dao().findByStatusAndTypeIdOrderByName(searchForm.getStatus(), searchForm.getType().getId(), pageable);
                } else {
                    page = this.dao().findByStatusOrderByName(searchForm.getStatus(), pageable);
                }
            } else {
                if (Objects.nonNull(searchForm.getType())) {
                    page = this.dao().findByTypeIdOrderByName(searchForm.getType().getId(), pageable);
                } else {
                    page = this.dao().findAll(pageable);
                }
            }
        }

        return new PageWrapper<>(page.map(a -> {
            AccountVO vo = this.converter().convert(a);
            vo.setBalance(this.accountTransactionService.retrieveAccountBalance(a.getId()).setScale(2, RoundingMode.DOWN));
            return vo;
        }), url);
    }

    @Override
    protected Supplier<Account> initEntitySupplier() {
        return Account::new;
    }

    @Override
    protected Supplier<AccountVO> initVOSupplier() {
        return AccountVO::new;
    }

    @Override
    public ServiceResponse validate(AccountVO vo, CRUD_OPERATION operation) {
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

    public void setBalanceThresholdForID(BigDecimal balanceThreshold, Long id) {
        this.dao().setBalanceThresholdForID(balanceThreshold, id);
    }
}
