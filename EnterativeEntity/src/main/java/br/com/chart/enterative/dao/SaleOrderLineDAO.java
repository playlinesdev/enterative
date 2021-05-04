package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SMS_STATUS;
import br.com.chart.enterative.repository.SaleOrderLineRepository;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SaleOrderLineDAO extends UserAwareDAO<SaleOrderLine, Long> {

    public SaleOrderLineDAO(BaseRepository<SaleOrderLine, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SaleOrderLineRepository repository() {
        return (SaleOrderLineRepository) super.repository();
    }
    
    public void setUserEmailStatusForId(EMAIL_STATUS status, Long id) {
        this.repository().setUserEmailStatusForId(status, id);
    }
    
    public void setSmsStatusForId(SMS_STATUS status, Long id) {
        this.repository().setSmsStatusForId(status, id);
    }

    public List<SaleOrderLine> findBySaleOrder(SaleOrder order) {
        return this.repository().findBySaleOrder(order);
    }

    public List<SaleOrderLine> findBySaleOrderAndStatus(SaleOrder pedido, SALE_ORDER_LINE_STATUS status) {
        return this.repository().findBySaleOrderAndStatus(pedido, status);
    }

    public SaleOrderLine findByExternalCode(String externalCode) {
        return this.repository().findByExternalCode(externalCode);
    }

    public List<SaleOrderLine> findBySaleOrderId(Long id) {
        return this.repository().findBySaleOrderId(id);
    }

    public long count(Example<SaleOrderLine> example) {
        return this.repository().count(example);
    }

    public SaleOrderLine findByBarcodeAndStatus(String barcode, SALE_ORDER_LINE_STATUS status) {
        return this.repository().findByBarcodeAndStatus(barcode, status);
    }

}
