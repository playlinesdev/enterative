package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.PagseguroTransactionResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PagseguroTransactionResponseDAO extends BaseDAO<PagseguroTransactionResponse, Long> {

    public PagseguroTransactionResponseDAO(BaseRepository<PagseguroTransactionResponse, Long> repository) {
        super(repository);
    }

}
