package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.CallbackQueue;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CallbackQueueDAO extends BaseDAO<CallbackQueue, Long> {

    public CallbackQueueDAO(BaseRepository<CallbackQueue, Long> repository) {
        super(repository);
    }

}
