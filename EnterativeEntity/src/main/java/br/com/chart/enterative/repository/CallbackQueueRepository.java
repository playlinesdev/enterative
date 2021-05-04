package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.CallbackQueue;

/**
 *
 * @author William Leite
 */
public interface CallbackQueueRepository extends BaseRepository<CallbackQueue, Long> {

    public CallbackQueue findFirstBy();
}
