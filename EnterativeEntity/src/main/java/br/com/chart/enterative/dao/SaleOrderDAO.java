package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.repository.SaleOrderRepository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SaleOrderDAO extends UserAwareDAO<SaleOrder, Long> {

    public SaleOrderDAO(BaseRepository<SaleOrder, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SaleOrderRepository repository() {
        return (SaleOrderRepository) super.repository();
    }
    
    public List<SaleOrder> findByEmailStatusAndTypeAndStatus(EMAIL_STATUS emailStatus, SALE_ORDER_TYPE type, SALE_ORDER_STATUS status) {
        return this.repository().findByEmailStatusAndTypeAndStatus(emailStatus, type, status);
    }

    public List<SaleOrder> findAll(Example<SaleOrder> example, Sort sort) {
        return this.repository().findAll(example, sort);
    }
    
    public List<SaleOrder> findByAccountIdOrderByCreatedAt(Long id) {
        return this.repository().findByAccountIdOrderByCreatedAt(id);
    }

    public void setEmailStatusForId(EMAIL_STATUS emailStatus, Long id) {
        this.repository().setEmailStatusForId(emailStatus, id);
    }

    public void setStatusForId(SALE_ORDER_STATUS orderStatus, Long id) {
        this.repository().setStatusForId(orderStatus, id);
    }

    public void setCustomerMobileForId(String customerMobile, Long id) {
        this.repository().setCustomerMobileForId(customerMobile, id);
    }

    public SaleOrder findByPaymentManagerToken(String paymentManagerToken) {
        return this.repository().findByPaymentManagerToken(paymentManagerToken);
    }

    public List<SaleOrder> findByCreatedByOrderByIdDesc(User user) {
        return this.repository().findByCreatedByOrderByIdDesc(user);
    }

    public List<SaleOrder> findByCreatedByIdOrderByIdDesc(Long userID) {
        return this.repository().findByCreatedByIdOrderByIdDesc(userID);
    }

    public SaleOrder findByIdEager(Long id) {
        return this.repository().findByIdEager(id);
    }
    
    public List<SaleOrder> findByStatus(SALE_ORDER_STATUS status) {
        return this.repository().findByStatus(status);
    }
    
    public List<SaleOrder> findByStatusEager(SALE_ORDER_STATUS status) {
        return this.repository().findByStatusEager(status);
    }
}
