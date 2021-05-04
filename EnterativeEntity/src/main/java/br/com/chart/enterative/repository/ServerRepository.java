package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import java.util.List;

import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.STATUS;

/**
 *
 * @author William Leite
 */
public interface ServerRepository extends UserAwareRepository<Server, Long> {

    public List<Server> findByStatus(STATUS status);

    public List<Server> findByStatusAndActivationProcessOrderBySequenceAsc(STATUS status, ACTIVATION_PROCESS activationProcess);
}
