package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.repository.PurchaseOrderLineRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PurchaseOrderLineDAO extends UserAwareDAO<PurchaseOrderLine, Long> {

    public PurchaseOrderLineDAO(BaseRepository<PurchaseOrderLine, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PurchaseOrderLineRepository repository() {
        return (PurchaseOrderLineRepository) super.repository();
    }

    public void setStatusForPurchaseOrderID(PURCHASE_ORDER_STATUS status, Long id) {
        this.repository().setStatusForPurchaseOrderID(status, id);
    }

    public void deleteByID(List<Long> ids) {
        this.repository().deleteByID(ids);
    }
}
