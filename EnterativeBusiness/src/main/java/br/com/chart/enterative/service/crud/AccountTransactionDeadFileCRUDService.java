package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.AccountDAO;
import br.com.chart.enterative.dao.AccountTransactionDeadFileDAO;
import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.enums.*;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.CRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountCommissionSearchVO;
import br.com.chart.enterative.vo.search.AccountTransactionSearchVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTransactionDeadFileCRUDService extends CRUDService<AccountTransactionDeadFile, Long, AccountTransactionVO, AccountTransactionSearchVO> {

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private AccountDAO accountDAO;

    public AccountTransactionDeadFileCRUDService(BaseDAO<AccountTransactionDeadFile, Long> dao, ConverterService<AccountTransactionDeadFile, AccountTransactionVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountTransactionDeadFileDAO dao() {
        return super.dao();
    }

    public AccountTransactionDeadFile findByPurchaseOrderLineId(Long id) {
        if (Objects.nonNull(id) && id > 0) {
            return this.dao().findByPurchaseOrderLineId(id);
        }
        return null;
    }

    public List<AccountTransactionDeadFile> findBySaleOrderLineId(Long id) {
        return this.dao().findBySaleOrderLineId(id);
    }

    public void cancel(Long id) {
        AccountTransactionDeadFile entity = this.findOne(id);

        entity.setStatus(ACCOUNT_TRANSACTION_STATUS.CANCELED);
        entity.setAlteredAt(new Date());
        entity.setAlteredBy(this.loggedUser());

        this.dao().saveAndFlush(entity);
    }

    public void activate(Long id) {
        AccountTransactionDeadFile entity = this.findOne(id);

        entity.setStatus(ACCOUNT_TRANSACTION_STATUS.ACTIVE);
        entity.setAlteredAt(new Date());
        entity.setAlteredBy(this.loggedUser());

        this.dao().saveAndFlush(entity);
    }

    public List<AccountTransactionDeadFile> retrieveTransactions(User user, AccountCommissionSearchVO searchForm) {
        Long accountID = this.retrieveAccountID(user, searchForm.getAccountId());
        Long categoryID = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_COMMISSION);
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());

        Specification<AccountTransactionDeadFile> specification = this.assembleSpecification(startDate, endDate, accountID, categoryID, null, null);
        return this.dao().repository().findAll(specification);
    }

    public List<AccountTransactionVO> retrieveTransactions(SalesByProductSearchVO searchForm) {
        Long accountID = Objects.nonNull(searchForm.getAccount()) ? searchForm.getAccount().getId() : null;
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());

        if (Objects.nonNull(accountID)) {
            List<Account> children = this.accountDAO.findByParentId(accountID);
            List<Long> ids = new ArrayList<>();
            ids.add(accountID);
            ids.addAll(children.stream().map(Account::getId).collect(Collectors.toList()));
            return this.retrieveTransactions(ids, startDate, endDate);
        } else {
            Specification<AccountTransactionDeadFile> specification = this.assembleSpecification(startDate, endDate, accountID, null, ACCOUNT_TRANSACTION_STATUS.ACTIVE, ACCOUNT_TRANSACTION_TYPE.DEBIT);
            List<AccountTransactionDeadFile> result = this.dao().repository().findAll(specification);
            return result.stream().map(this.converter()::convert).collect(Collectors.toList());
        }
    }

    private List<AccountTransactionVO> retrieveTransactions(List<Long> ids, Date startDate, Date endDate) {
        List<AccountTransactionDeadFile> result;
        if (Objects.nonNull(startDate)) {
            if (Objects.nonNull(endDate)) {
                result = this.dao().findByAccountInAndTransactionDate(ids, startDate, endDate);
            } else {
                result = this.dao().findByAccountInAndTransactionDateGreaterThan(ids, startDate);
            }
        } else if (Objects.nonNull(endDate)) {
            result = this.dao().findByAccountInAndTransactionDateLessThan(ids, endDate);
        } else {
            result = this.dao().findByAccountIn(ids);
        }

        return result.stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public Specification<AccountTransactionDeadFile> assembleSpecification(final Date startDate, final Date endDate, final Long accountID, final Long categoryID,
                                                                   final ACCOUNT_TRANSACTION_STATUS status, final ACCOUNT_TRANSACTION_TYPE type) {
        return (Root<AccountTransactionDeadFile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(startDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate)));
            }
            if (Objects.nonNull(endDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate)));
            }
            if (Objects.nonNull(accountID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("account").get("id"), accountID)));
            }
            if (Objects.nonNull(categoryID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("category").get("id"), categoryID)));
            }
            if (Objects.nonNull(status)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status)));
            }
            if (Objects.nonNull(type)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), type)));
            }

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Transactional
    public List<AccountTransactionVO> retrieveTransactions(User user, AccountTransactionSearchVO searchForm) {
        Long accountID = this.retrieveAccountID(user, searchForm.getAccount());
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());
        Long categoryID = Objects.nonNull(searchForm.getCategory()) ? searchForm.getCategory().getId() : null;
        ACCOUNT_TRANSACTION_STATUS status = searchForm.getStatus();
        ACCOUNT_TRANSACTION_TYPE type = searchForm.getType();

        Specification<AccountTransactionDeadFile> specification = this.assembleSpecification(startDate, endDate, accountID, categoryID, status, type);
        List<AccountTransactionDeadFile> result = this.dao().repository().findAll(specification);
        return result.stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public AccountTransactionDeadFile convert(AccountTransaction transaction) {
        AccountTransactionDeadFile deadFile = new AccountTransactionDeadFile();
        deadFile.setAccount(transaction.getAccount());
        deadFile.setAmount(transaction.getAmount());
        deadFile.setCategory(transaction.getCategory());
        deadFile.setId(transaction.getId());
        deadFile.setPurchaseOrderLine(transaction.getPurchaseOrderLine());
        deadFile.setSaleOrderLine(transaction.getSaleOrderLine());
        deadFile.setStatus(transaction.getStatus());
        deadFile.setTransactionDate(transaction.getTransactionDate());
        deadFile.setType(transaction.getType());
        deadFile.setCreatedBy(transaction.getCreatedBy());
        return deadFile;
    }

    @Override
    protected Supplier<AccountTransactionDeadFile> initEntitySupplier() {
        return AccountTransactionDeadFile::new;
    }

    @Override
    protected Supplier<AccountTransactionVO> initVOSupplier() {
        return AccountTransactionVO::new;
    }

    @Override
    public ServiceResponse validate(AccountTransactionVO vo, CRUD_OPERATION operation) {
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

    public List<AccountTransactionDeadFile> retrieveTransactions(Specification<AccountTransactionDeadFile> accountTransactionDeadFileSpecification) {
        return this.dao().repository().findAll(accountTransactionDeadFileSpecification);
    }
}
