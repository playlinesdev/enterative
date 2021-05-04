package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.EpayTransaction;
import br.com.chart.enterative.repository.EpayTransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayTransactionDAO extends UserAwareDAO<EpayTransaction, Long> {

    public EpayTransactionDAO(BaseRepository<EpayTransaction, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EpayTransactionRepository repository() {
        return (EpayTransactionRepository) super.repository();
    }
    
    public List<EpayTransaction> findByActivationId(Long id) {
        return this.repository().findByActivationId(id);
    }
}
