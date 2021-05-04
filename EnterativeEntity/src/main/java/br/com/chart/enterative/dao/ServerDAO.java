package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.ServerRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ServerDAO extends UserAwareDAO<Server, Long> {

    public ServerDAO(BaseRepository<Server, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ServerRepository repository() {
        return (ServerRepository) super.repository();
    }

    public List<Server> findByStatus(STATUS status) {
        return this.repository().findByStatus(status);
    }

    public List<Server> findByStatusAndActivationProcessOrderBySequenceAsc(STATUS status, ACTIVATION_PROCESS activationProcess) {
        return this.repository().findByStatusAndActivationProcessOrderBySequenceAsc(status, activationProcess);
    }

}
