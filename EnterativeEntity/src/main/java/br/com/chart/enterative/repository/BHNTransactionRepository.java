package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 */
public interface BHNTransactionRepository extends UserAwareRepository<BHNTransaction, Long> {

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.localTransactionDate = :transactionDate ORDER BY t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveByDate(@Param("direction") TRANSACTION_DIRECTION direction, @Param("transactionDate") String localTransactionDate);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.localTransactionDate IN :transactionDate "
            + " AND t.responseCode = :responseCode ORDER BY t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveByDate(@Param("direction") TRANSACTION_DIRECTION direction, @Param("transactionDate") List<String> dates, @Param("responseCode") String responseCode);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.localTransactionDate = :transactionDate "
            + "AND t.id NOT IN (SELECT d.bhnTransaction FROM SDFDetail d WHERE d.bhnTransaction IS NOT NULL) "
            + "ORDER BY t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveMissingByDate(@Param("direction") TRANSACTION_DIRECTION direction, @Param("transactionDate") String localTransactionDate);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.localTransactionDate IN :transactionDate "
            + "AND t.id NOT IN (SELECT d.bhnTransaction FROM SDFDetail d WHERE d.bhnTransaction IS NOT NULL) AND t.responseCode = :responseCode "
            + "ORDER BY t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveMissingByDate(@Param("direction") TRANSACTION_DIRECTION direction, @Param("transactionDate") List<String> localTransactionDate, @Param("responseCode") String responseCode);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.localTransactionDate = :transactionDate "
            + "AND t.merchantTerminalId LIKE :terminalId AND t.id NOT IN (SELECT d.bhnTransaction FROM SDFDetail d WHERE d.bhnTransaction IS NOT NULL) "
            + "ORDER BY t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveByDate(@Param("direction") TRANSACTION_DIRECTION direction, @Param("transactionDate") String localTransactionDate, @Param("terminalId") String terminalId);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.primaryAccountNumber = :number "
            + "AND t.merchantTerminalId LIKE :terminalId AND t.id NOT IN (SELECT d.bhnTransaction FROM SDFDetail d WHERE d.bhnTransaction IS NOT NULL) "
            + "ORDER BY t.localTransactionDate ASC, t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveByGift(@Param("direction") TRANSACTION_DIRECTION direction, @Param("number") String primaryAccountNumber, @Param("terminalId") String merchantTerminalId);

    @Query("SELECT t FROM BHNTransaction t WHERE t.direction = :direction AND t.productId = :productId "
            + "AND t.merchantTerminalId LIKE :terminalId AND t.id NOT IN (SELECT d.bhnTransaction FROM SDFDetail d WHERE d.bhnTransaction IS NOT NULL) "
            + "ORDER BY t.localTransactionDate ASC, t.localTransactionTime ASC")
    public List<BHNTransaction> retrieveByProduct(@Param("direction") TRANSACTION_DIRECTION direction, @Param("productId") String productId, @Param("terminalId") String merchantTerminalId);

    @Query("SELECT t FROM BHNTransaction t WHERE t.localTransactionDate = :transactionDate AND t.localTransactionTime = :transactionTime "
            + "AND t.merchantTerminalId = :terminalId AND t.primaryAccountNumber = :primaryAccountNumber "
            + "AND t.productId = :productId AND t.responseCode = :responseCode "
            + "AND t.systemTraceAuditNumber = :systemTraceAuditNumber AND t.direction = :direction")
    public BHNTransaction retrieveByMatchPrimaryAccountNumber(@Param("transactionDate") String localTransactionDate, @Param("transactionTime") String localTransactionTime,
            @Param("terminalId") String terminalId, @Param("primaryAccountNumber") String primaryAccountNumber, @Param("productId") String productId,
            @Param("responseCode") String responseCode, @Param("systemTraceAuditNumber") String systemTraceAuditNumber, @Param("direction") TRANSACTION_DIRECTION direction);
    
    @Query("SELECT t FROM BHNTransaction t WHERE t.localTransactionDate = :transactionDate AND t.localTransactionTime = :transactionTime "
            + "AND t.merchantTerminalId = :terminalId AND t.activationAccountNumber = :activationAccountNumber "
            + "AND t.productId = :productId AND t.responseCode = :responseCode "
            + "AND t.systemTraceAuditNumber = :systemTraceAuditNumber AND t.direction = :direction")
    public BHNTransaction retrieveByMatchActivationAccountNumber(@Param("transactionDate") String localTransactionDate, @Param("transactionTime") String localTransactionTime,
            @Param("terminalId") String terminalId, @Param("activationAccountNumber") String activationAccountNumber, @Param("productId") String productId,
            @Param("responseCode") String responseCode, @Param("systemTraceAuditNumber") String systemTraceAuditNumber, @Param("direction") TRANSACTION_DIRECTION direction);
    

    public List<BHNTransaction> findByResponseCode(String responseCode);

    public List<BHNTransaction> findByBhnActivationAndDirectionOrderByCreatedAtDesc(BHNActivation ativacao, TRANSACTION_DIRECTION direction);

    public List<BHNTransaction> findByBhnActivationId(Long id);    
    
    public Page<BHNTransaction> findByProductId(String productId, Pageable pageable);
    
    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceID, String primaryAccountNumber, String redemptionPin, Pageable Pageable);    

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceID, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceID, Pageable pageable);

//    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, String redemptionPin, Pageable pageable);

//    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdOrderByCreatedAtOrder(String productId, TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable);

    public Page<BHNTransaction> findByRedemptionPinOrderByCreatedAtDesc(String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByPrimaryAccountNumberOrderByCreatedAtDesc(String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByResourceIdOrderByCreatedAtDesc(Long resourceId, Pageable pageable);

    public Page<BHNTransaction> findByResourceIdAndRedemptionPinOrderByCreatedAtDesc(Long resourceId, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(Long resourceId, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByDirectionOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndResourceIdOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdOrderByCreatedAtDesc(String productId, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndRedemptionPinOrderByCreatedAtDesc(String productId, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndResourceIdOrderByCreatedAtDesc(String productId, Long resourceId, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(String productId, Long resourceId, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, Long resourceId, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String primaryAccountNumber, Pageable pageable);

    public Page<BHNTransaction> findByProductIdAndDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String primaryAccountNumber, String redemptionPin, Pageable pageable);

    public Page<BHNTransaction> findBySystemTraceAuditNumberOrderByCreatedAtDesc(String systemTraceAuditNumber, Pageable pageable);
}
