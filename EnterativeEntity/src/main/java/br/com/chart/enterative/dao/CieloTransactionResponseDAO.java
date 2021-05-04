package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.entity.CieloTransactionResponse;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CieloTransactionResponseDAO extends BaseDAO<CieloTransactionResponse, Long> {

    public CieloTransactionResponseDAO(BaseRepository<CieloTransactionResponse, Long> repository) {
        super(repository);
    }

}
