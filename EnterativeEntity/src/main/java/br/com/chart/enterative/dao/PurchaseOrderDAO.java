package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.repository.PurchaseOrderRepository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PurchaseOrderDAO extends UserAwareDAO<PurchaseOrder, Long> {

    public PurchaseOrderDAO(BaseRepository<PurchaseOrder, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PurchaseOrderRepository repository() {
        return (PurchaseOrderRepository) super.repository();
    }

    public List<PurchaseOrder> findByAccountIdOrderByCreatedAt(Long accountId) {
        return this.repository().findByAccountIdOrderByCreatedAt(accountId);
    }

    public void activateForID(PURCHASE_ORDER_STATUS status, User user, Date date, Long id) {
        this.repository().activateForID(status, user, date, id);
    }
}
