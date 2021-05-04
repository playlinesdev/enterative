package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.SaleOrderDAO;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.USER_ROLE;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.SaleOrderSearchVO;

import java.util.*;
import java.util.function.Supplier;

import br.com.chart.enterative.vo.search.SalesByProductSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class SaleOrderCRUDService extends UserAwareCRUDService<SaleOrder, Long, SaleOrderVO, SaleOrderSearchVO> {

    @Autowired
    private SaleOrderLineDAO saleOrderLineDAO;

    @Autowired
    private EnterativeUtils utils;

    public SaleOrderCRUDService(UserAwareDAO<SaleOrder, Long> dao, ConverterService<SaleOrder, SaleOrderVO> converter) {
        super(dao, converter);
    }

    @Override
    public SaleOrderDAO dao() {
        return (SaleOrderDAO) super.dao();
    }

    public List<SaleOrder> findByStatus(SALE_ORDER_STATUS status) {
        return this.dao().findByStatus(status);
    }

    public List<SaleOrder> findByEmailStatusAndTypeAndStatus(EMAIL_STATUS emailStatus, SALE_ORDER_TYPE type, SALE_ORDER_STATUS status) {
        return this.dao().findByEmailStatusAndTypeAndStatus(emailStatus, type, status);
    }

    public List<SaleOrder> findByAccountIdOrderByCreatedAt(Long id) {
        return this.dao().findByAccountIdOrderByCreatedAt(id);
    }

    public List<SaleOrder> findAll(Example<SaleOrder> example, Sort sort) {
        return this.dao().findAll(example, sort);
    }

    public void setEmailStatusForId(EMAIL_STATUS emailStatus, Long id) {
        this.dao().setEmailStatusForId(emailStatus, id);
    }

    public void setStatusForId(SALE_ORDER_STATUS orderStatus, Long id) {
        this.dao().setStatusForId(orderStatus, id);
    }

    public void setCustomerMobileForId(String customerMobile, Long id) {
        this.dao().setCustomerMobileForId(customerMobile, id);
    }

    public SaleOrder findByPaymentManagerToken(String paymentManagerToken) {
        return this.dao().findByPaymentManagerToken(paymentManagerToken);
    }

    public SaleOrder findByCreatedByOrderByIdDesc(User user) {
        return this.dao().findByCreatedByOrderByIdDesc(user).stream().findFirst().orElse(null);
    }

    public SaleOrder findByCreatedByIdOrderByIdDesc(Long userID) {
        return this.dao().findByCreatedByIdOrderByIdDesc(userID).stream().findFirst().orElse(null);
    }

    public SaleOrder findByIdEager(Long id) {
        return this.dao().findByIdEager(id);
    }
    // ------------------ //     

    public void cancel(Long id) {
        SaleOrder order = this.findOne(id);
        if (Objects.nonNull(order)) {
            order.setStatus(SALE_ORDER_STATUS.DENIED);
            order.getLines().forEach(l -> l.setStatus(SALE_ORDER_LINE_STATUS.DENIED));
            this.dao().saveAndFlush(order, this.loggedUserId());
        }
    }

    public void activate(Long id) {
        SaleOrder order = this.findOne(id);
        if (Objects.nonNull(order)) {
            order.setStatus(SALE_ORDER_STATUS.ACTIVATED);
            this.dao().saveAndFlush(order, this.loggedUserId());
        }
    }

    public SaleOrder removeExternalCode(Long id) {
        SaleOrderLine line = this.saleOrderLineDAO.findOne(id);
        if (Objects.nonNull(line)) {
            line.setExternalCode("--");
            line = this.saleOrderLineDAO.saveAndFlush(line, this.loggedUserId());
            return line.getSaleOrder();
        } else {
            return null;
        }
    }

    public Specification<SaleOrder> assembleSpecification(final Long id, final Date startDate, final Date endDate,
                                                          final Long accountID, final Long shopID,
                                                          final SALE_ORDER_STATUS[] status, final SALE_ORDER_TYPE type) {
        return (Root<SaleOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(id)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), id)));
            }
            if (Objects.nonNull(startDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate)));
            }
            if (Objects.nonNull(endDate)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate)));
            }
            if (Objects.nonNull(accountID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("account").get("id"), accountID)));
            }
            if (Objects.nonNull(shopID)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shop").get("id"), shopID)));
            }
            if (Objects.nonNull(status)) {
                CriteriaBuilder.In<SALE_ORDER_STATUS> inClause = criteriaBuilder.in(root.get("status"));
                Arrays.stream(status).forEach(inClause::value);
                predicates.add(inClause);
            }
            if (Objects.nonNull(type)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("type"), type)));
            }

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdAt")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public PageWrapper<SaleOrderVO> retrieveOrders(User user, SaleOrderSearchVO searchForm, Pageable pageable, String url) {
        Long shopID = null;
        Long accountID = null;
        if (this.userDAO.hasRole(user, USER_ROLE.ROLE_ADMIN)) {
            if (Objects.nonNull(searchForm.getShop())) {
                shopID = searchForm.getShop().getId();
            }
            if (Objects.nonNull(searchForm.getAccount())) {
                accountID = searchForm.getAccount().getId();
            }
        } else {
            shopID = user.getShop().getId();
            accountID = user.getAccount().getId();
        }
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());
        Long id = searchForm.getId();
        SALE_ORDER_STATUS[] statuses = null;
        if (Objects.nonNull(searchForm.getStatus())) {
            statuses = new SALE_ORDER_STATUS[]{searchForm.getStatus()};
        }

        Specification<SaleOrder> specification = this.assembleSpecification(id, startDate, endDate, accountID, shopID, statuses, searchForm.getType());
        Page<SaleOrder> page = this.dao().repository().findAll(specification, pageable);
        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public SALE_ORDER_LINE_STATUS retrieveLineStatusWithResponse(SaleOrderLine line, RESPONSE_CODE responseCode, RESPONSE_CODE responseAux, ACTIVATION_STATUS ativacaoStatus) {
        SALE_ORDER_LINE_STATUS result;
        if (ativacaoStatus == ACTIVATION_STATUS.PROCESSED && responseCode == RESPONSE_CODE.E00) {
            if (line.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN && (responseAux == RESPONSE_CODE.B00 || responseAux == RESPONSE_CODE.B01)) {
                result = SALE_ORDER_LINE_STATUS.ACTIVATED;
            } else if (line.getProduct().getActivationProcess() == ACTIVATION_PROCESS.EPAY && (responseAux == RESPONSE_CODE.P0)) {
                result = SALE_ORDER_LINE_STATUS.ACTIVATED;
            } else {
                result = SALE_ORDER_LINE_STATUS.DENIED;
            }
        } else if (ativacaoStatus == ACTIVATION_STATUS.PROCESSED) {
            result = SALE_ORDER_LINE_STATUS.DENIED;
        } else {
            result = SALE_ORDER_LINE_STATUS.PENDING;
        }

        return result;
    }

    public SALE_ORDER_STATUS retrieveOrderStatus(List<SaleOrderLine> lines) {
        SALE_ORDER_STATUS result;

        boolean isDenied = lines.stream().allMatch(l -> l.getStatus() == SALE_ORDER_LINE_STATUS.DENIED);
        boolean isActivated = lines.stream().allMatch(l -> l.getStatus() == SALE_ORDER_LINE_STATUS.ACTIVATED);

        if (isActivated) {
            result = SALE_ORDER_STATUS.ACTIVATED;
        } else if (isDenied) {
            result = SALE_ORDER_STATUS.DENIED;
        } else {
            result = SALE_ORDER_STATUS.PENDING;
        }

        return result;
    }

    public SALE_ORDER_STATUS retrieveOrderStatus(SaleOrder order) {
        List<SaleOrderLine> lines = this.saleOrderLineDAO.findBySaleOrder(order);
        return this.retrieveOrderStatus(lines);
    }

    @Override
    public ServiceResponse validate(SaleOrderVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<SaleOrder> initEntitySupplier() {
        return () -> {
            SaleOrder entity = new SaleOrder();
            entity.setEmailStatus(EMAIL_STATUS.IDLE);
            return entity;
        };
    }

    @Override
    protected Supplier<SaleOrderVO> initVOSupplier() {
        return SaleOrderVO::new;
    }

    public List<SaleOrder> retrieveOrders(SalesByProductSearchVO searchForm) {
        Date startDate = this.utils.firstMoment(searchForm.getStartDate());
        Date endDate = this.utils.lastMoment(searchForm.getEndDate());
        Long accountID = Objects.nonNull(searchForm.getAccount()) ? searchForm.getAccount().getId() : null;

        Specification<SaleOrder> specification = this.assembleSpecification(null, startDate, endDate, accountID, null, new SALE_ORDER_STATUS[]{SALE_ORDER_STATUS.ACTIVATED}, null);
        return this.retrieveOrders(specification);
    }

    public List<SaleOrder> retrieveOrders(final Specification<SaleOrder> specification) {
        return this.dao().repository().findAll(specification);
    }
}
