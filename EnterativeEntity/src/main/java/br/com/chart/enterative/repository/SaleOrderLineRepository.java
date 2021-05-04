package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SMS_STATUS;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface SaleOrderLineRepository extends UserAwareRepository<SaleOrderLine, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE SaleOrderLine SET userEmailStatus = :status WHERE id = :id")
    public void setUserEmailStatusForId(@Param("status") EMAIL_STATUS status, @Param("id") Long id);
    
    @Modifying
    @Transactional
    @Query("UPDATE SaleOrderLine SET smsStatus = :status WHERE id = :id")
    public void setSmsStatusForId(@Param("status") SMS_STATUS status, @Param("id") Long id);
    
    @Query("SELECT s FROM SaleOrderLine s LEFT JOIN FETCH s.createdBy LEFT JOIN FETCH s.alteredBy WHERE s.externalCode = :externalCode")
    public SaleOrderLine findByExternalCode(@Param("externalCode") String externalCode);

    public List<SaleOrderLine> findBySaleOrderAndStatus(SaleOrder pedido, SALE_ORDER_LINE_STATUS status);

    public List<SaleOrderLine> findBySaleOrder(SaleOrder order);

    public List<SaleOrderLine> findBySaleOrderId(Long id);

    public SaleOrderLine findByBarcodeAndStatus(String barcode, SALE_ORDER_LINE_STATUS status);
}
