package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.SaleOrderLine;

import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionCategory;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_TYPE;
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
public interface AccountTransactionRepository extends UserAwareRepository<AccountTransaction, Long>, JpaSpecificationExecutor<AccountTransaction> {

    public AccountTransaction findBySaleOrderLineAndType(SaleOrderLine line, ACCOUNT_TRANSACTION_TYPE type);

    @Modifying
    @Transactional
    @Query("UPDATE AccountTransaction SET status = :status, altered_at = :altered_at, altered_by = :altered_by WHERE id = :id")
    public void setStatusById(@Param("status") ACCOUNT_TRANSACTION_STATUS status, @Param("id") Long id, @Param("altered_at") Date alteredAt, @Param("altered_by") User alteredBy);

    @Modifying
    @Transactional
    @Query("DELETE FROM AccountTransaction WHERE id IN :id")
    public void deleteByID(@Param("id") List<Long> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM AccountTransaction WHERE purchaseOrderLine IS NULL AND saleOrderLine IS NULL")
    public void deleteByLastPosition();

    // ---
    @Query("SELECT t FROM AccountTransaction t WHERE t.account.id = :id AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByAccount(@Param("id") Long id);

    // ---
    @Query("SELECT t FROM AccountTransaction t WHERE t.transactionDate < :endDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByTransactionDateLessThan(@Param("endDate") Date endDate);

    public AccountTransaction findByPurchaseOrderLineId(Long id);

    public List<AccountTransaction> findBySaleOrderLineId(Long id);

    //--
    @Query("SELECT t FROM AccountTransaction t WHERE t.account.id IN :id AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByAccountIn(@Param("id") List<Long> id);

    @Query("SELECT t FROM AccountTransaction t WHERE t.account.id IN :id AND t.transactionDate BETWEEN :startDate AND :endDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByAccountInAndTransactionDate(@Param("id") List<Long> id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT t FROM AccountTransaction t WHERE t.account.id IN :id AND t.transactionDate > :startDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByAccountInAndTransactionDateGreaterThan(@Param("id") List<Long> id, @Param("startDate") Date startDate);

    @Query("SELECT t FROM AccountTransaction t WHERE t.account.id IN :id AND t.transactionDate < :endDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransaction> findByAccountInAndTransactionDateLessThan(@Param("id") List<Long> id, @Param("endDate") Date endDate);
}
