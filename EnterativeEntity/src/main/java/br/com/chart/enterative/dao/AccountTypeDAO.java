package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.AccountType;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTypeDAO extends UserAwareDAO<AccountType, Long> {
    
    public AccountTypeDAO(BaseRepository<AccountType, Long> repository) {
        super(repository);
    }
    
}
