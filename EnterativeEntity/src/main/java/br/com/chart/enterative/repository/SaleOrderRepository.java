package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface SaleOrderRepository extends UserAwareRepository<SaleOrder, Long>, JpaSpecificationExecutor<SaleOrder> {

    @Query("SELECT SUM(amount) FROM SaleOrder WHERE shop.id = :shop AND status = :status AND createdAt BETWEEN :start AND :end")
    BigDecimal sumByShopAndStatus(@Param("shop") Long shop, @Param("status") SALE_ORDER_STATUS status, @Param("start") Date start, @Param("end") Date end);

    @Modifying
    @Transactional
    @Query("UPDATE SaleOrder SET status = :status WHERE id = :id")
    public void setStatusForId(@Param("status") SALE_ORDER_STATUS status, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SaleOrder SET customerMobile = :mobile WHERE id = :id")
    public void setCustomerMobileForId(@Param("mobile") String mobile, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SaleOrder SET emailStatus = :status WHERE id = :id")
    public void setEmailStatusForId(@Param("status") EMAIL_STATUS status, @Param("id") Long id);

    @Query("SELECT o FROM SaleOrder o LEFT JOIN FETCH o.shop LEFT JOIN FETCH o.alteredBy LEFT JOIN FETCH o.createdBy LEFT JOIN FETCH o.lines WHERE o.id = :id")
    public SaleOrder findByIdEager(@Param("id") Long id);

    @Query("SELECT o FROM SaleOrder o LEFT JOIN FETCH o.shop LEFT JOIN FETCH o.alteredBy LEFT JOIN FETCH o.createdBy LEFT JOIN FETCH o.lines WHERE o.status = :status")
    public List<SaleOrder> findByStatusEager(@Param("status") SALE_ORDER_STATUS status);

    public List<SaleOrder> findByStatus(SALE_ORDER_STATUS status);

    public List<SaleOrder> findByAccountIdOrderByCreatedAt(Long accountID);

    public SaleOrder findByPaymentManagerToken(String paymentManagerToken);

    public List<SaleOrder> findByCreatedByOrderByIdDesc(User user);

    public List<SaleOrder> findByCreatedByIdOrderByIdDesc(Long userID);
    
    public List<SaleOrder> findByEmailStatusAndTypeAndStatus(EMAIL_STATUS emailStatus, SALE_ORDER_TYPE type, SALE_ORDER_STATUS status);
}
