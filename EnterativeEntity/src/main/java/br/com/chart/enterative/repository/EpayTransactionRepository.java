package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.EpayTransaction;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface EpayTransactionRepository extends UserAwareRepository<EpayTransaction, Long> {

    public List<EpayTransaction> findByActivationId(Long id);
}
