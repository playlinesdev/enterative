package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
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
public interface PurchaseOrderRepository extends UserAwareRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {

   @Modifying
    @Transactional
    @Query("UPDATE PurchaseOrder SET status = :status, activatedUser = :auser, activatedDate = :adate WHERE id = :id")
    void activateForID(@Param("status") PURCHASE_ORDER_STATUS status, @Param("auser") User user, @Param("adate") Date date, @Param("id") Long id);

    List<PurchaseOrder> findByAccountIdOrderByCreatedAt(Long accountId);
}
