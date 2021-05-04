package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountTransactionCategory;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTransactionCategoryDAO extends UserAwareDAO<AccountTransactionCategory, Long> {

    public AccountTransactionCategoryDAO(BaseRepository<AccountTransactionCategory, Long> repository) {
        super(repository);
    }
}
