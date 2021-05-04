package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface PurchaseOrderLineRepository extends UserAwareRepository<PurchaseOrderLine, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE PurchaseOrderLine SET status = :status WHERE purchaseOrder.id = :id")
    public void setStatusForPurchaseOrderID(@Param("status") PURCHASE_ORDER_STATUS status, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PurchaseOrderLine WHERE id IN :id")
    public void deleteByID(@Param("id") List<Long> id);
}
