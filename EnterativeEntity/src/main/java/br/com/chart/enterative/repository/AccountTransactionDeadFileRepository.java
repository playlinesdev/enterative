package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author William Leite
 */
public interface AccountTransactionDeadFileRepository extends BaseRepository<AccountTransactionDeadFile, Long>, JpaSpecificationExecutor<AccountTransactionDeadFile> {

    @Query("SELECT t FROM AccountTransactionDeadFile t WHERE t.account.id = :id AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransactionDeadFile> findByAccount(@Param("id") Long id);

    public AccountTransactionDeadFile findByPurchaseOrderLineId(Long id);

    public List<AccountTransactionDeadFile> findBySaleOrderLineId(Long id);

    //--
    @Query("SELECT t FROM AccountTransactionDeadFile t WHERE t.account.id IN :id AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransactionDeadFile> findByAccountIn(@Param("id") List<Long> id);

    @Query("SELECT t FROM AccountTransactionDeadFile t WHERE t.account.id IN :id AND t.transactionDate BETWEEN :startDate AND :endDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDate(@Param("id") List<Long> id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT t FROM AccountTransactionDeadFile t WHERE t.account.id IN :id AND t.transactionDate > :startDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDateGreaterThan(@Param("id") List<Long> id, @Param("startDate") Date startDate);

    @Query("SELECT t FROM AccountTransactionDeadFile t WHERE t.account.id IN :id AND t.transactionDate < :endDate AND (t.saleOrderLine IS NOT NULL OR t.purchaseOrderLine IS NOT NULL) ORDER BY t.transactionDate ASC")
    public List<AccountTransactionDeadFile> findByAccountInAndTransactionDateLessThan(@Param("id") List<Long> id, @Param("endDate") Date endDate);
}
