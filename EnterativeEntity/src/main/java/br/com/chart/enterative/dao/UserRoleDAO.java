package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.UserRole;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class UserRoleDAO extends UserAwareDAO<UserRole, Long> {

    public UserRoleDAO(BaseRepository<UserRole, Long> repository) {
        super(repository);
    }

}
