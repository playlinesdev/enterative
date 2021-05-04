package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.AccountDAO;
import br.com.chart.enterative.dao.AccountTransactionCategoryDAO;
import br.com.chart.enterative.dao.AccountTransactionDAO;
import br.com.chart.enterative.dao.AccountTransactionDeadFileDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionCategory;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.PurchaseOrderLineVO;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
import br.com.chart.enterative.entity.vo.SaleOrderLineVO;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.repository.SaleOrderLineRepository;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountCommissionSearchVO;
import br.com.chart.enterative.vo.search.AccountTransactionSearchGroupingVO;
import br.com.chart.enterative.vo.search.AccountTransactionSearchVO;
import br.com.chart.enterative.vo.search.SalesByProductSearchVO;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author William Leite
 */
@Service
public class AccountTransactionCRUDService extends UserAwareCRUDService<AccountTransaction, Long, AccountTransactionVO, AccountTransactionSearchVO> {

    @Autowired
    private AccountTransactionDeadFileDAO deadFileDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private ShopProductCommissionCRUDService shopProductCommissionCRUDService;

    @Autowired
    private AccountTransactionCategoryDAO accountTransactionCategoryDAO;

    @Autowired
    private SaleOrderLineRepository saleOrderLineRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AccountTransactionCRUDService(UserAwareDAO<AccountTransaction, Long> dao, ConverterService<AccountTransaction, AccountTransactionVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountTransactionDAO dao() {
        return (AccountTransactionDAO) super.dao();
    }

    public boolean exists(final Long id) {
        return this.dao().exists(id);
    }

    // Repository Links
    public List<AccountTransaction> findByTransactionDateLessThanOrderByTransactionDateAsc(Date endDate) {
        return this.dao().findByTransactionDateLessThanOrderByTransactionDateAsc(endDate);
    }

    public AccountTransaction findByPurchaseOrderLineId(Long id) {
        return this.dao().findByPurchaseOrderLineId(id);
    }

    public List<AccountTransaction> findBySaleOrderLineId(Long id) {
        return this.dao().findBySaleOrderLineId(id);
    }

    public void deleteByID(List<Long> ids) {
        this.dao().deleteByID(ids);
    }

    public void deleteByLastPosition() {
        this.dao().deleteByLastPosition();
    }

    @Override
    public AccountTransactionVO initVO() {
        AccountTransactionVO vo = super.initVO();
        vo.setTransactionDate(new Date());
        vo.setPurchaseOrderLine(new PurchaseOrderLineVO());
        vo.setSaleOrderLine(new SaleOrderLineVO());
        return vo;
    }

    @Override
    public ServiceResponse processSave(AccountTransactionVO vo, Long user) throws CRUDServiceException {
        Long categoryID = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_COMMISSION);
        if (Objects.equals(vo.getCategory().getId(), categoryID)) {
            Optional<SaleOrderLine> line = this.saleOrderLineRepository.findById(vo.getSaleOrderLine().getId());
            PurchaseOrderVO purchaseOrder = this.shopProductCommissionCRUDService.createCommission(line.get()).get("purchaseOrder");
            return new ServiceResponse().put("entity", this.converter().convert(this.findByPurchaseOrderLineId(purchaseOrder.getLines().get(0).getId())));
        }
        return super.processSave(vo, user);
    }

    // -----------------

    public void cancel(AccountTransaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.setStatus(ACCOUNT_TRANSACTION_STATUS.CANCELED);
            this.dao().saveAndFlush(transaction, this.loggedUserId());
        }
    }

    public void activate(AccountTransaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.setStatus(ACCOUNT_TRANSACTION_STATUS.ACTIVE);
            this.dao().saveAndFlush(transaction, this.loggedUserId());
        }
    }

    private Map<String, List<AccountTransactionVO>> groupList(List<AccountTransactionVO> list, Long groupingID) {
        Map<String, List<AccountTransactionVO>> hm = null;

        if (groupingID == 1L) {
            hm = list.stream().collect(Collectors.groupingBy(v -> v.getAccount().getName()));
        } else if (groupingID == 2L) {
            hm = list.stream().collect(Collectors.groupingBy(v -> v.getProductName()));
        }

        return hm;
    }

    public Map<String, Object> groupList(List<AccountTransactionVO> list, AccountTransactionSearchGroupingVO[] grouping) {
        Map<String, Object> hm = new HashMap<>();

        boolean byAccount = Arrays.stream(grouping).anyMatch(g -> Objects.equals(g.getId(), 1L) && g.isSelected());
        boolean byProduct = Arrays.stream(grouping).anyMatch(g -> Objects.equals(g.getId(), 2L) && g.isSelected());

        if (byAccount) {
            Map<String, List<AccountTransactionVO>> hmAccount = this.groupList(list, 1L);
            if (byProduct) {
                hmAccount.entrySet().stream().forEach((Map.Entry<String, List<AccountTransactionVO>> e) -> {
                    hm.put(e.getKey(), this.groupList(e.getValue(), 2L));
                });
            } else {
                hmAccount.entrySet().stream().forEach(e -> {
                    hm.put(e.getKey(), e.getValue());
                });
            }
        } else if (byProduct) {
            this.groupList(list, 2L).entrySet().stream().forEach(e -> {
                hm.put(e.getKey(), e.getValue());
            });
        }

        return hm;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> totalGroupList(Map<String, Object> hm) {
        Map<String, Object> totals = new HashMap<>();

        hm.entrySet().forEach((Map.Entry<String, Object> e) -> {
            if (e.getValue() instanceof Map) {
                Map<String, Object> hmKeyTotal = new HashMap<>();
                ((Map<String, List<AccountTransactionVO>>) e.getValue()).entrySet().forEach(ie -> {
                    hmKeyTotal.put(ie.getKey(), new Object[]{
                            ie.getValue().stream().map(AccountTransactionVO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                            ie.getValue().stream().count()
                    });
                });
                BigDecimal total = hmKeyTotal.values().stream().map(o -> ((BigDecimal) ((Object[]) o)[0])).reduce(BigDecimal.ZERO, BigDecimal::add);
                long count = hmKeyTotal.values().stream().map(o -> ((long) ((Object[]) o)[1])).reduce(0L, Long::sum);
                hmKeyTotal.put(String.format("%s-total", e.getKey()), total);
                hmKeyTotal.put(String.format("%s-count", e.getKey()), count);

                totals.put(e.getKey(), hmKeyTotal);
            } else if (e.getValue() instanceof List) {
                totals.put(e.getKey(), new Object[]{
                        ((List<AccountTransactionVO>) e.getValue()).stream().map(AccountTransactionVO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add),
                        ((List<AccountTransactionVO>) e.getValue()).stream().count()
                });
            }
        });

        return totals;
    }

    public AccountTransactionSearchVO initSearchVO() {
        AccountTransactionSearchVO vo = new AccountTransactionSearchVO();
        vo.setGrouping(new AccountTransactionSearchGroupingVO[]{
                new AccountTransactionSearchGroupingVO(1L, "base.account"),
                new AccountTransactionSearchGroupingVO(2L, "base.product")
        });
        return vo;
    }

    public BigDecimal retrieveLastPositionAmount(Account account, AccountTransactionSearchVO searchForm, Boolean isReport) {
        List<BigDecimal> amounts = new ArrayList<>();
        List<AccountTransaction> transactions = this.dao().findByAccount(account.getId());
        Date minTransaction = transactions.stream().map(AccountTransaction::getTransactionDate).filter(Objects::nonNull).min(Date::compareTo).orElse(null);

        if (Objects.nonNull(transactions)) {
            amounts.addAll(transactions.stream()
                    .filter(t -> t.getStatus() == ACCOUNT_TRANSACTION_STATUS.ACTIVE
                            || (t.getStatus() == ACCOUNT_TRANSACTION_STATUS.PENDING && Objects.nonNull(t.getSaleOrderLine()) && t.getSaleOrderLine().getSaleOrder().getType() == SALE_ORDER_TYPE.PERSONALLY)) // PENDING and ACTIVE
                    .filter((AccountTransaction t) -> {
                        if (!isReport) {
                            if (Objects.nonNull(searchForm.getStartDate())) {
                                if (Objects.nonNull(minTransaction)) {
                                    return t.getTransactionDate().before(
                                            Arrays.stream(new Date[]{searchForm.getStartDate(), minTransaction})
                                                    .max(Date::compareTo)
                                                    .orElse(new Date())
                                    );
                                } else {
                                    return false;
                                }
                            } else if (Objects.nonNull(searchForm.getEndDate())) {
                                return t.getTransactionDate().before(searchForm.getEndDate());
                            } else {
                                return false;
                            }
                        } else if (Objects.nonNull(searchForm.getStartDate())) {
                            return t.getTransactionDate().before(searchForm.getStartDate());
                        } else {
                            return false;
                        }
                    })
                    .map(this::asBigDecimal)
                    .collect(Collectors.toList()));
        }

        List<AccountTransactionDeadFile> deadFiles = this.deadFileDAO.findByAccount(account.getId());
        if (Objects.nonNull(deadFiles)) {
            amounts.addAll(deadFiles.stream()
                    .filter(t -> t.getStatus() == ACCOUNT_TRANSACTION_STATUS.ACTIVE
                            || (t.getStatus() == ACCOUNT_TRANSACTION_STATUS.PENDING && Objects.nonNull(t.getSaleOrderLine()) && t.getSaleOrderLine().getSaleOrder().getType() == SALE_ORDER_TYPE.PERSONALLY))
                    .filter((AccountTransactionDeadFile t) -> {
                        if (!isReport) {
                            if (Objects.nonNull(searchForm.getStartDate())) {
                                if (Objects.nonNull(minTransaction)) {
                                    return t.getTransactionDate().before(
                                            Arrays.stream(new Date[]{searchForm.getStartDate(), minTransaction})
                                                    .max(Date::compareTo)
                                                    .orElse(new Date())
                                    );
                                } else {
                                    return true;
                                }
                            } else if (Objects.nonNull(searchForm.getEndDate())) {
                                return t.getTransactionDate().before(searchForm.getEndDate());
                            } else {
                                return true;
                            }
                        } else if (Objects.nonNull(searchForm.getStartDate())) {
                            return t.getTransactionDate().before(searchForm.getStartDate());
                        } else {
                            return false;
                        }
                    })
                    .map(this::asBigDecimal)
                    .collect(Collectors.toList()));
        }

        return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateLastPosition(Account account) {
        BigDecimal lastPosition = this.retrieveLastPositionAmount(account, new AccountTransactionSearchVO(), false);
        ACCOUNT_TRANSACTION_TYPE type;

        if (lastPosition.signum() >= 0) {
            type = ACCOUNT_TRANSACTION_TYPE.CREDIT;
        } else {
            type = ACCOUNT_TRANSACTION_TYPE.DEBIT;
            lastPosition = lastPosition.negate();
        }

        User systemUser = this.systemUser();
        this.add(account, lastPosition, type, systemUser, null, null, ACCOUNT_TRANSACTION_STATUS.ACTIVE, null);
    }

    public void updateLastPosition() {
        this.deleteByLastPosition();

        List<Account> accounts = this.accountDAO.findByStatusOrderByName(STATUS.ACTIVE);
        accounts.parallelStream().forEach(this::updateLastPosition);
    }

    private Map<ACCOUNT_TRANSACTION_TYPE, Map<ACCOUNT_TRANSACTION_STATUS, BigDecimal>> populateTotalsMap() {
        final Map<ACCOUNT_TRANSACTION_TYPE, Map<ACCOUNT_TRANSACTION_STATUS, BigDecimal>> totals = new LinkedHashMap<>();

        ACCOUNT_TRANSACTION_TYPE.ordered().stream().forEach(t -> {
            Map<ACCOUNT_TRANSACTION_STATUS, BigDecimal> inner = new LinkedHashMap<>();
            ACCOUNT_TRANSACTION_STATUS.ordered().stream().forEach(s -> {
                inner.put(s, BigDecimal.ZERO);
            });
            totals.put(t, inner);
        });

        return totals;
    }

    public ServiceResponse retrieveTotals(List<AccountTransactionVO> transactions) {
        if (Objects.nonNull(transactions)) {
            final Map<ACCOUNT_TRANSACTION_TYPE, Map<ACCOUNT_TRANSACTION_STATUS, BigDecimal>> totals = populateTotalsMap();

            transactions.stream().forEach((AccountTransactionVO t) -> {
                BigDecimal total = totals.get(t.getType()).get(t.getStatus());
                total = total.add(t.getAmount());
                totals.get(t.getType()).put(t.getStatus(), total);
            });

            return new ServiceResponse().put("totals", totals);
        } else {
            return new ServiceResponse().setMessage("Transações vazias");
        }
    }

    public AccountTransactionVO retrieveLastPositionVO(Long id, AccountTransactionSearchVO searchForm, Boolean isReport) {
        Account account = this.accountDAO.findOne(id);
        return this.retrieveLastPositionVO(account, searchForm, isReport);
    }

    private AccountTransactionVO retrieveLastPositionVO(Account account, AccountTransactionSearchVO searchForm, Boolean isReport) {
        BigDecimal amount = this.retrieveLastPositionAmount(account, searchForm, isReport);
        ACCOUNT_TRANSACTION_TYPE type = amount.signum() >= 0 ? ACCOUNT_TRANSACTION_TYPE.CREDIT : ACCOUNT_TRANSACTION_TYPE.DEBIT;

        AccountTransaction transaction = this.createTransaction(account, amount.abs(), type, null, null, null, ACCOUNT_TRANSACTION_STATUS.ACTIVE, null);
        transaction.setTransactionDate(null);
        return this.converter().convert(transaction);
    }

    public List<AccountTransaction> retrieveTransactions(User user, AccountCommissionSearchVO searchForm) {
        Long accountID = this.retrieveAccountID(user, searchForm.getAccountId());
        Long categoryID = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_COMMISSION);
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());

        Specification<AccountTransaction> specification = this.assembleSpecification(startDate, endDate, accountID, categoryID, null, null);
        return this.dao().repository().findAll(specification);
    }

    private List<AccountTransactionVO> retrieveTransactions(List<Long> ids, Date startDate, Date endDate) {
        List<AccountTransaction> result;
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

    public List<AccountTransaction> retrieveTransactions(final Specification<AccountTransaction> specification) {
        return this.dao().repository().findAll(specification);
    }

    private List<AccountTransactionVO> retrieveTransactions(Long accountID, Date startDate, Date endDate, ACCOUNT_TRANSACTION_STATUS status, ACCOUNT_TRANSACTION_TYPE type) {
        Specification<AccountTransaction> specification = this.assembleSpecification(startDate, endDate, accountID, null, status, type, true);
        List<AccountTransaction> result = this.dao().repository().findAll(specification);
        return result.stream().map(this.converter()::convert).collect(Collectors.toList());
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
            return this.retrieveTransactions(accountID, startDate, endDate, ACCOUNT_TRANSACTION_STATUS.ACTIVE, ACCOUNT_TRANSACTION_TYPE.DEBIT);
        }
    }

    public Specification<AccountTransaction> assembleSpecification(final Date startDate, final Date endDate, final Long accountID, final Long categoryID,
                                                                   final ACCOUNT_TRANSACTION_STATUS status, final ACCOUNT_TRANSACTION_TYPE type) {
        return this.assembleSpecification(startDate, endDate, accountID, categoryID, status, type, false);
    }

    public Specification<AccountTransaction> assembleSpecification(final Date startDate, final Date endDate, final Long accountID, final Long categoryID,
                                                                   final ACCOUNT_TRANSACTION_STATUS status, final ACCOUNT_TRANSACTION_TYPE type, final Boolean removeLastPosition) {
        return (Root<AccountTransaction> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
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
            if (removeLastPosition) {
                Predicate notNullSale = criteriaBuilder.isNotNull(root.get("saleOrderLine"));
                Predicate notNullPurchase = criteriaBuilder.isNotNull(root.get("purchaseOrderLine"));
                Predicate notNullCategory = criteriaBuilder.isNotNull(root.get("category"));
                predicates.add(criteriaBuilder.or(criteriaBuilder.or(notNullSale, notNullPurchase), notNullCategory));
            }

            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("transactionDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public List<AccountTransactionVO> retrieveTransactions(User user, AccountTransactionSearchVO searchForm) {
        Long accountID = this.retrieveAccountID(user, searchForm.getAccount());
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());
        Long categoryID = Objects.nonNull(searchForm.getCategory()) ? searchForm.getCategory().getId() : null;
        ACCOUNT_TRANSACTION_STATUS status = searchForm.getStatus();
        ACCOUNT_TRANSACTION_TYPE type = searchForm.getType();

        Specification<AccountTransaction> specification = this.assembleSpecification(startDate, endDate, accountID, categoryID, status, type);
        return this.dao().repository().findAll(specification).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public PageWrapper<AccountTransactionVO> retrieveTransactions(User user, AccountTransactionSearchVO searchForm, Pageable pageable, String url) {
        Long accountID = this.retrieveAccountID(user, searchForm.getAccount());
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());

        Specification<AccountTransaction> specification = this.assembleSpecification(startDate, endDate, accountID, null, null, null, true);
        Page<AccountTransaction> page = this.dao().repository().findAll(specification, pageable);
        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public BigDecimal retrieveAccountBalance(Long accountID) {
        Account account = this.accountDAO.findByIdWithTransactionsEager(accountID);
        BigDecimal result = BigDecimal.ZERO;

        if (Objects.nonNull(account) && Objects.nonNull(account.getTransactions())) {
            result = account.getTransactions().stream()
                    .filter(t -> t.getStatus() == ACCOUNT_TRANSACTION_STATUS.ACTIVE
                            || (t.getStatus() == ACCOUNT_TRANSACTION_STATUS.PENDING && Objects.nonNull(t.getSaleOrderLine()) && t.getSaleOrderLine().getSaleOrder().getType() == SALE_ORDER_TYPE.PERSONALLY)) // PENDING and ACTIVE
                    .map(this::asBigDecimal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return result;
    }

    private BigDecimal asBigDecimal(BigDecimal amount, ACCOUNT_TRANSACTION_TYPE type) {
        if (type == ACCOUNT_TRANSACTION_TYPE.CREDIT) {
            return amount;
        } else {
            return amount.negate();
        }
    }

    private BigDecimal asBigDecimal(AccountTransaction transaction) {
        return this.asBigDecimal(transaction.getAmount(), transaction.getType());
    }

    private BigDecimal asBigDecimal(AccountTransactionDeadFile transaction) {
        return this.asBigDecimal(transaction.getAmount(), transaction.getType());
    }

    public ServiceResponse processSaleOrderLine(SaleOrderLine line, SALE_ORDER_LINE_STATUS status, boolean isCallback) {
        String result = null;
        Long systemUser = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SYSTEM_USER);

        // Callback has no authentication, therefore no logged user
        User user = isCallback ? this.reflectionUtils.asHollowLink(User::new, systemUser) : this.loggedUser();

        switch (status) {
            // new pending transaction
            case PENDING:
                if (!isCallback) {
                    result = this.add(line).getMessage();
                }
                break;
            // update existing transaction to active
            case ACTIVATED:
                if (Objects.isNull(line.getSaleOrder().getPaymentManagerToken()) || line.getSaleOrder().getPaymentManagerToken().isEmpty()) {
                    result = this.change(line, ACCOUNT_TRANSACTION_STATUS.ACTIVE, user).getMessage();
                }
                // inserts commission transaction
                if (Objects.isNull(result)) {
                    result = this.shopProductCommissionCRUDService.createCommission(line).getMessage();
                }
                break;
            // update existing transaction to canceled
            case DENIED:
                result = this.change(line, ACCOUNT_TRANSACTION_STATUS.CANCELED, user).getMessage();
                break;
        }

        return new ServiceResponse().setMessage(result);
    }

    public ServiceResponse processPurchaseOrder(PurchaseOrder order, PURCHASE_ORDER_STATUS status, SaleOrderLine line) {
        ServiceResponse result = null;

        switch (status) {
            case ACTIVE:
                result = order.getLines().stream()
                        .map((PurchaseOrderLine l) -> {
                            return this.addCommission(l, line);
                        })
                        .filter(r -> Objects.nonNull(r.getMessage()))
                        .findFirst().orElse(null);
                break;
        }

        return Objects.isNull(result) ? new ServiceResponse().setMessage(null) : result;
    }

    public ServiceResponse processPurchaseOrder(PurchaseOrder order, PURCHASE_ORDER_STATUS status) {
        ServiceResponse result = null;

        switch (status) {
            case ACTIVE:
                result = order.getLines().stream()
                        .map(l -> this.add(l, order))
                        .filter(r -> Objects.nonNull(r.getMessage()))
                        .findFirst().orElse(null);
                break;
        }

        return Objects.isNull(result) ? new ServiceResponse().setMessage(null) : result;
    }

    private AccountTransaction createTransaction(Account account, BigDecimal amount, ACCOUNT_TRANSACTION_TYPE type, User user, SaleOrderLine saleLine, PurchaseOrderLine purchaseLine, ACCOUNT_TRANSACTION_STATUS status, AccountTransactionCategory category) {
        AccountTransaction transaction = new AccountTransaction();

        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setPurchaseOrderLine(purchaseLine);
        transaction.setSaleOrderLine(saleLine);
        transaction.setStatus(status);
        transaction.setTransactionDate(new Date());
        transaction.setType(type);
        transaction.setCreatedBy(user);
        transaction.setCreatedAt(new Date());

        return transaction;
    }

    private ServiceResponse add(Account account, BigDecimal amount, ACCOUNT_TRANSACTION_TYPE type, User user, SaleOrderLine saleLine, PurchaseOrderLine purchaseLine, ACCOUNT_TRANSACTION_STATUS status, AccountTransactionCategory category) {
        String result = null;
        try {
            log.debug("Criou uma transação");
            AccountTransaction transaction = this.createTransaction(account, amount, type, user, saleLine, purchaseLine, status, category);
            this.dao().saveAndFlush(transaction, user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return new ServiceResponse().setMessage(result);
    }

    private ServiceResponse add(Account account, BigDecimal amount, PurchaseOrderLine purchaseOrderLine, ACCOUNT_TRANSACTION_TYPE type, User user, AccountTransactionCategory category) {
        return this.add(account, amount, type, user, null, purchaseOrderLine, ACCOUNT_TRANSACTION_STATUS.ACTIVE, category);
    }

    private ServiceResponse add(Account account, BigDecimal amount, SaleOrderLine saleOrderLine, ACCOUNT_TRANSACTION_TYPE type, User user, AccountTransactionCategory category) {
        return this.add(account, amount, type, user, saleOrderLine, null, ACCOUNT_TRANSACTION_STATUS.PENDING, category);
    }

    private ServiceResponse add(SaleOrderLine line) {
        SaleOrder order = line.getSaleOrder();
        User user = order.getCreatedBy();
        Account account = user.getAccount();

        AccountTransactionCategory category = this.accountTransactionCategoryDAO.findOne(this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_SALE));
        if (Objects.isNull(category)) {
            return new ServiceResponse().setMessage("Categoria de Transação de Venda não configurada!");
        }

        return this.add(account, line.getAmount(), line, ACCOUNT_TRANSACTION_TYPE.DEBIT, user, category);
    }

    private ServiceResponse addCommission(PurchaseOrderLine purchaseLine, SaleOrderLine saleLine) {
        Account account = purchaseLine.getPurchaseOrder().getAccount();
        User user = purchaseLine.getPurchaseOrder().getCreatedBy();

        AccountTransactionCategory category = this.accountTransactionCategoryDAO.findOne(this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_COMMISSION));
        if (Objects.isNull(category)) {
            return new ServiceResponse().setMessage("Categoria de Transação de Comissão não configurada!");
        }

        return this.add(account, purchaseLine.getAmount(), ACCOUNT_TRANSACTION_TYPE.CREDIT, user, saleLine, purchaseLine, ACCOUNT_TRANSACTION_STATUS.ACTIVE, category);
    }

    private ServiceResponse add(PurchaseOrderLine line, PurchaseOrder order) {
        Account account = order.getAccount();
        User user = this.loggedUser();

        Long id = this.parameterDAO.get(ENVIRONMENT_PARAMETER.SHOP_TRANSACTION_CATEGORY_PURCHASE);
        AccountTransactionCategory category = this.accountTransactionCategoryDAO.findOne(id);
        if (Objects.isNull(category)) {
            return new ServiceResponse().setMessage("Categoria de Transação de Compra não configurada!");
        }

        return this.add(account, line.getAmount(), line, ACCOUNT_TRANSACTION_TYPE.CREDIT, user, category);
    }

    private ServiceResponse change(Long id, ACCOUNT_TRANSACTION_STATUS status, User user) {
        String result = null;
        try {
            this.dao().setStatusById(status, id, new Date(), user);
        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return new ServiceResponse().setMessage(result);
    }

    private ServiceResponse change(SaleOrderLine line, ACCOUNT_TRANSACTION_STATUS status, User user) {
        try {
            AccountTransaction transaction = this.dao().findBySaleOrderLineAndType(line, ACCOUNT_TRANSACTION_TYPE.DEBIT);
            if (Objects.nonNull(transaction)) {
                if (transaction.getStatus() != status) {
                    return this.change(transaction.getId(), status, user);
                } else {
                    return new ServiceResponse();
                }
            } else {
                //Todo Validate this !!!
                if (line.getSaleOrder().getType() != SALE_ORDER_TYPE.VIRTUAL) {
                    return new ServiceResponse().setMessage("Não foi possível encontrar a transação");
                } else {
                    return new ServiceResponse();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResponse().setMessage(e.getMessage());
        }
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

    @Override
    protected Supplier<AccountTransaction> initEntitySupplier() {
        return AccountTransaction::new;
    }

    @Override
    protected Supplier<AccountTransactionVO> initVOSupplier() {
        return AccountTransactionVO::new;
    }
}
