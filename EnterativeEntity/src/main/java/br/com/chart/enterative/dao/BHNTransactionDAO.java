package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.repository.BHNTransactionRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNTransactionDAO extends UserAwareDAO<BHNTransaction, Long> {

    public BHNTransactionDAO(BaseRepository<BHNTransaction, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BHNTransactionRepository repository() {
        return (BHNTransactionRepository) super.repository();
    }

    public List<BHNTransaction> findByBhnActivationId(Long id) {
        return this.repository().findByBhnActivationId(id);
    }

    public List<BHNTransaction> findByBhnActivationAndDirectionOrderByCreatedAtDesc(BHNActivation ativacao, TRANSACTION_DIRECTION direction) {
        return this.repository().findByBhnActivationAndDirectionOrderByCreatedAtDesc(ativacao, direction);
    }

    public List<BHNTransaction> retrieveMissingByDate(String date) {
        return this.repository().retrieveMissingByDate(TRANSACTION_DIRECTION.RETURN, date);
    }

    public List<BHNTransaction> retrieveMissingByDate(List<String> dates, String responseCode) {
        return this.repository().retrieveMissingByDate(TRANSACTION_DIRECTION.RETURN, dates, responseCode);
    }

    public List<BHNTransaction> retrieveByDate(String date) {
        return this.repository().retrieveByDate(TRANSACTION_DIRECTION.RETURN, date);
    }

    public List<BHNTransaction> retrieveByDate(List<String> date, String responseCode) {
        return this.repository().retrieveByDate(TRANSACTION_DIRECTION.RETURN, date, responseCode);
    }

    public List<BHNTransaction> retrieveByDate(String localTransactionDate, String terminalId) {
        return this.repository().retrieveByDate(TRANSACTION_DIRECTION.RETURN, localTransactionDate, terminalId);
    }

    public List<BHNTransaction> retrieveByGift(String primaryAccountNumber, String merchantTerminalId) {
        return this.repository().retrieveByGift(TRANSACTION_DIRECTION.RETURN, primaryAccountNumber, merchantTerminalId);
    }

    public List<BHNTransaction> retrieveByProduct(String productId, String merchantTerminalId) {
        return this.repository().retrieveByProduct(TRANSACTION_DIRECTION.RETURN, productId, merchantTerminalId);
    }

    public BHNTransaction retrieveByMatchPrimaryAccountNumber(String localTransactionDate, String localTransactionTime, String terminalId, String primaryAccountNumber,
            String productId, String responseCode, String systemTraceAuditNumber, TRANSACTION_DIRECTION direction) {
        return this.repository().retrieveByMatchPrimaryAccountNumber(localTransactionDate, localTransactionTime, terminalId, primaryAccountNumber, productId, responseCode, systemTraceAuditNumber, direction);
    }

    public BHNTransaction retrieveByMatchActivationAccountNumber(String localTransactionDate, String localTransactionTime, String terminalId, String activationAccountNumber,
            String productId, String responseCode, String systemTraceAuditNumber, TRANSACTION_DIRECTION direction) {
        return this.repository().retrieveByMatchActivationAccountNumber(localTransactionDate, localTransactionTime, terminalId, activationAccountNumber, productId, responseCode, systemTraceAuditNumber, direction);
    }

    public Page<BHNTransaction> retrieveByProductID(String product_id, Pageable pageable) {
        return this.repository().findByProductId(product_id, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId, direction, resourceId, primaryAccountNumber, redemptionPin, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(productId, direction, resourceId, primaryAccountNumber, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdOrderByCreatedAtDesc(productId, direction, resourceId, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId, direction, resourceId, primaryAccountNumber, redemptionPin, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(productId, direction, resourceId, redemptionPin, pageable);
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndResourceIdAndOrderByCreatedAtOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndResourceIdOrderByCreatedAtDesc(productId,
                 direction, resourceId,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId,
                 direction, primaryAccountNumber,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(productId,
                 direction, primaryAccountNumber,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndDirectionAndRedemptionPinOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionAndRedemptionPinOrderByCreatedAtDesc(productId,
                 direction, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndDirectionOrderByCreatedAtDesc(String productId, TRANSACTION_DIRECTION direction, Pageable pageable) {
        return this.repository().findByProductIdAndDirectionOrderByCreatedAtDesc(productId,
                 direction, pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderbyCreatedAtDesc(String productId, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId,
                 resourceId, primaryAccountNumber,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndResourceIdAndPrimaryAccountNumberOrderbyCreatedAtDesc(String productId, Long resourceId, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByProductIdAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(productId,
                 resourceId, primaryAccountNumber,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndResourceIdAndRedemptionPinOrderbyCreatedAtDesc(String productId, Long resourceId, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(productId,
                 resourceId, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndResourceIdOrderbyCreatedAtDesc(String productId, Long resourceId, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndResourceIdOrderByCreatedAtDesc(productId,
                 resourceId, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String productId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(productId,
                 primaryAccountNumber, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndPrimaryAccountNumberOrderByCreatedAtDesc(String productId, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByProductIdAndPrimaryAccountNumberOrderByCreatedAtDesc(productId,
                 primaryAccountNumber, pageable
        );
    }

    public Page<BHNTransaction> findByProductIdAndRedemptionPinOrderByCreatedAtDesc(String productId, String redemptionPin, Pageable pageable) {
        return this.repository().findByProductIdAndRedemptionPinOrderByCreatedAtDesc(productId,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByProductIdOrderByCreatedAtDesc(String productId, Pageable pageable) {
        return this.repository().findByProductIdOrderByCreatedAtDesc(productId,
                 pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByDirectionAndResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(direction,
                 resourceId, primaryAccountNumber,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByDirectionAndResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(direction,
                 resourceId, primaryAccountNumber,
                 pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, String redemptionPin, Pageable pageable) {
        return this.repository().findByDirectionAndResourceIdAndRedemptionPinOrderByCreatedAtDesc(direction,
                 resourceId, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndResourceIdOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Long resourceId, Pageable pageable) {
        return this.repository().findByDirectionAndResourceIdOrderByCreatedAtDesc(direction,
                 resourceId, pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByDirectionAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(direction,
                 primaryAccountNumber, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByDirectionAndPrimaryAccountNumberOrderByCreatedAtDesc(direction,
                 primaryAccountNumber, pageable
        );
    }

    public Page<BHNTransaction> findByDirectionAndRedemptionPinOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, String redemptionPin, Pageable pageable) {
        return this.repository().findByDirectionAndRedemptionPinOrderByCreatedAtDesc(direction,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByDirectionOrderByCreatedAtDesc(TRANSACTION_DIRECTION direction, Pageable pageable) {
        return this.repository().findByDirectionOrderByCreatedAtDesc(direction,
                 pageable
        );
    }

    public Page<BHNTransaction> findByResourceIdAndprimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(Long resourceId, String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByResourceIdAndPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(resourceId,
                 primaryAccountNumber, redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findByResourceIdAndprimaryAccountNumberOrderByCreatedAtDesc(Long resourceId, String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByResourceIdAndPrimaryAccountNumberOrderByCreatedAtDesc(resourceId,
                 primaryAccountNumber, pageable
        );
    }

    public Page<BHNTransaction> findByResourceIdAndRedemptionPinOrderByCreatedAtDesc(Long resourceId, String redemptionPin, Pageable pageable) {
        return this.repository().findByResourceIdAndRedemptionPinOrderByCreatedAtDesc(resourceId,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByResourceIdOrderByCreatedAtDesc(Long resourceId, Pageable pageable) {
        return this.repository().findByResourceIdOrderByCreatedAtDesc(resourceId,
                 pageable
        );
    }

    public Page<BHNTransaction> findByPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(String primaryAccountNumber, String redemptionPin, Pageable pageable) {
        return this.repository().findByPrimaryAccountNumberAndRedemptionPinOrderByCreatedAtDesc(primaryAccountNumber,
                 redemptionPin, pageable
        );
    }

    public Page<BHNTransaction> findByPrimaryAccountNumberOrderByCreatedAtDesc(String primaryAccountNumber, Pageable pageable) {
        return this.repository().findByPrimaryAccountNumberOrderByCreatedAtDesc(primaryAccountNumber,
                 pageable
        );
    }

    public Page<BHNTransaction> findByRedemptionPinOrderByCreatedAtDesc(String redemptionPin, Pageable pageable) {
        return this.repository().findByRedemptionPinOrderByCreatedAtDesc(redemptionPin,
                 pageable
        );
    }

    public Page<BHNTransaction> findBySystemTraceAuditNumberOrderByCreatedAtDesc(String systemTraceAuditNumber, Pageable pageable) {
        return this.repository().findBySystemTraceAuditNumberOrderByCreatedAtDesc(systemTraceAuditNumber, pageable);
    }
}
