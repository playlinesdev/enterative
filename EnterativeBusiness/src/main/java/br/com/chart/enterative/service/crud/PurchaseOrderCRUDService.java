package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.PurchaseOrderDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.*;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PurchaseOrderSearchVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class PurchaseOrderCRUDService extends UserAwareCRUDService<PurchaseOrder, Long, PurchaseOrderVO, PurchaseOrderSearchVO> {

    @Autowired
    private PurchaseOrderLineCRUDService purchaseOrderLineCRUDService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService accountTransactionDeadFileService;

    @Autowired
    private EnterativeUtils utils;

    public PurchaseOrderCRUDService(UserAwareDAO<PurchaseOrder, Long> dao, ConverterService<PurchaseOrder, PurchaseOrderVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PurchaseOrderDAO dao() {
        return (PurchaseOrderDAO) super.dao();
    }

    public List<PurchaseOrder> findByAccountIdOrderByCreatedAt(Long id) {
        return this.dao().findByAccountIdOrderByCreatedAt(id);
    }

    public PurchaseOrderVO convertForUI(PurchaseOrderVO vo) {
        vo.setAccountTransactions(new ArrayList<>(0));

        vo.getLines().forEach((l) -> {
            AccountTransaction transaction = this.accountTransactionService.findByPurchaseOrderLineId(l.getId());
            AccountTransactionDeadFile transactionDeadFile = this.accountTransactionDeadFileService.findByPurchaseOrderLineId(l.getId());
            if (Objects.nonNull(transaction)) {
                vo.getAccountTransactions().add(this.accountTransactionService.converter().convert(transaction));
            } else if (Objects.nonNull(transactionDeadFile)) {
                vo.getAccountTransactions().add(this.accountTransactionDeadFileService.converter().convert(transactionDeadFile));
            }
        });

        return vo;
    }

    public PurchaseOrder initEntity(Shop shop, Account account, User user, BigDecimal totalAmount, PURCHASE_ORDER_STATUS status) {
        PurchaseOrder order = new PurchaseOrder();
        order.setAccount(account);
        order.setActivatedDate(new Date());
        order.setActivatedUser(user);
        order.setCreatedAt(new Date());
        order.setLines(new ArrayList<>());
        order.setShop(shop);
        order.setStatus(status);
        order.setTotalAmount(totalAmount);
        order.setCreatedBy(user);
        return order;
    }

    public PurchaseOrderVO initVO(UserVO user) {
        PurchaseOrderVO order = new PurchaseOrderVO();
        order.setCreatedAt(new Date());
        order.setLines(new ArrayList<>());
        order.setStatus(PURCHASE_ORDER_STATUS.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setCreatedBy(user);
        order.setNewLine(this.purchaseOrderLineCRUDService.initVO());
        return order;
    }

    public ServiceResponse addLine(PurchaseOrderVO orderVO) {
        ServiceResponse result = new ServiceResponse();

        try {
            this.fill(orderVO);
            if (Objects.isNull(orderVO.getLines())) {
                orderVO.setLines(new ArrayList<>());
            }

            orderVO.getLines().add(this.purchaseOrderLineCRUDService.initVO());
        } catch (Exception e) {
            result.setMessage("Ocorreu um erro: %s", e.getMessage());
        } finally {
            result.put("entity", orderVO);
        }

        return result;
    }

    public ServiceResponse removeLine(PurchaseOrderVO orderVO, Long id) {
        ServiceResponse result = new ServiceResponse();

        try {
            this.fill(orderVO);
            if (Objects.isNull(orderVO.getLines())) {
                orderVO.setLines(new ArrayList<>());
            }

            orderVO.setLines(
                orderVO.getLines().stream()
                .filter(p -> !Objects.equals(p.getId(), id))
                .collect(Collectors.toList())
            );
        } catch (Exception e) {
            result.setMessage("Ocorreu um erro: %s", e.getMessage());
        } finally {
            result.put("entity", orderVO);
        }

        return result;
    }

    public Specification<PurchaseOrder> assembleSpecification(Long accountID, PURCHASE_ORDER_STATUS status, boolean isCommission) {
        return (Root<PurchaseOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(accountID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("account").get("id"), accountID)));
            }
            if (Objects.nonNull(status)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status)));
            }
            if (isCommission) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("activatedUser").get("id"), this.systemUserId())));
            } else {
                predicates.add(criteriaBuilder.and(criteriaBuilder.notEqual(root.get("activatedUser").get("id"), this.systemUserId())));
            }

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<PurchaseOrder> assembleSpecification(Long shopID, Long accountID, Date startDate, Date endDate, PURCHASE_ORDER_STATUS status) {
        return (Root<PurchaseOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(shopID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shop").get("id"), shopID)));
            }
            if (Objects.nonNull(accountID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("account").get("id"), accountID)));
            }
            if (Objects.nonNull(startDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate)));
            }
            if (Objects.nonNull(endDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate)));
            }
            if (Objects.nonNull(status)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), status)));
            }

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public List<PurchaseOrder> retrieveOrders(final Specification<PurchaseOrder> specification) {
        return this.dao().repository().findAll(specification);
    }

    public PageWrapper<PurchaseOrderVO> retrieveOrders(User user, PurchaseOrderSearchVO searchForm, Pageable pageable, String url) {
        Long shopID = null;
        Long accountID = null;
        if (this.userDAO.hasRole(user, USER_ROLE.ROLE_ADMIN)) {
            if (Objects.nonNull(searchForm.getShop())) {
                shopID = searchForm.getShop().getId();
            }
            if (Objects.nonNull(searchForm.getAccount())) {
                accountID = searchForm.getAccount().getId();
            }
        } else if (this.userDAO.hasRole(user, USER_ROLE.ROLE_SHOP_ADMIN)) {
            shopID = user.getShop().getId();
            accountID = user.getAccount().getId();
        }

        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());

        Specification<PurchaseOrder> specification = this.assembleSpecification(shopID, accountID, startDate, endDate, null);
        Page<PurchaseOrder> page = this.dao().repository().findAll(specification, pageable);
        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public ServiceResponse activateCommissionOrder(Long orderID, User user, SaleOrderLine line) {
        this.dao().activateForID(PURCHASE_ORDER_STATUS.ACTIVE, user, new Date(), orderID);
        this.purchaseOrderLineCRUDService.setStatusForPurchaseOrderID(PURCHASE_ORDER_STATUS.ACTIVE, orderID);

        PurchaseOrder order = this.dao().findOne(orderID);
        ServiceResponse result = this.accountTransactionService.processPurchaseOrder(order, PURCHASE_ORDER_STATUS.ACTIVE, line);
        result.put("purchaseOrder", this.converter().convert(order));
        return result;
    }

    public boolean purchaseOrderHasTransactions(PurchaseOrder order){
        List<AccountTransaction> transactions = new ArrayList<>();
        for(PurchaseOrderLine line : order.getLines()){
            AccountTransaction transaction = accountTransactionService.dao().findByPurchaseOrderLineId(line.getId());
            if(transaction != null)
                transactions.add(transaction);
        }
        return transactions.size() > 0;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse activateOrder(Long orderID, User user) {
        ServiceResponse result;
        try {
            PurchaseOrder order = this.dao().findOne(orderID);
            this.dao().activateForID(PURCHASE_ORDER_STATUS.ACTIVE, user, new Date(), orderID);
            this.purchaseOrderLineCRUDService.setStatusForPurchaseOrderID(PURCHASE_ORDER_STATUS.ACTIVE, orderID);

            result = this.accountTransactionService.processPurchaseOrder(order, PURCHASE_ORDER_STATUS.ACTIVE);
            result.put("entity", this.converter().convert(order));
        } catch (Exception e) {
            result = new ServiceResponse().setMessage(e.getMessage());
            throw new CRUDServiceException(result);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {CRUDServiceException.class})
    public ServiceResponse processSave(PurchaseOrderVO purchaseOrder, Long user) throws CRUDServiceException {
        if (Objects.isNull(purchaseOrder.getLines())) {
            purchaseOrder.setLines(new ArrayList<>());
        }

        PurchaseOrder order = this.converter().convert(purchaseOrder);
        this.fill(order);
        order.setTotalAmount(order.getLines().stream().map(PurchaseOrderLine::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        order = this.dao().saveAndFlush(order, user);
        purchaseOrder = this.converter().convert(order);

        return new ServiceResponse().put("entity", purchaseOrder);
    }

    public void cancel(Long id) {
        PurchaseOrder order = this.findOne(id);
        if (Objects.nonNull(order)) {
            order.setStatus(PURCHASE_ORDER_STATUS.DENIED);
            order.getLines().forEach(l -> l.setStatus(PURCHASE_ORDER_STATUS.DENIED));
            this.dao().saveAndFlush(order, this.loggedUserId());
        }
    }

    @Override
    public ServiceResponse validate(PurchaseOrderVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<PurchaseOrder> initEntitySupplier() {
        return PurchaseOrder::new;
    }

    @Override
    protected Supplier<PurchaseOrderVO> initVOSupplier() {
        return PurchaseOrderVO::new;
    }
}
