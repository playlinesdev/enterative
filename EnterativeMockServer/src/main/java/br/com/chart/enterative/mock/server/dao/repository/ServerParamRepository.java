package br.com.chart.enterative.mock.server.dao.repository;

import br.com.chart.enterative.mock.server.entity.ServerParam;
import br.com.chart.enterative.mock.server.entity.enums.SERVER_PARAMS;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author William Leite
 */
public interface ServerParamRepository extends JpaRepository<ServerParam, Long> {
    public ServerParam findByName(SERVER_PARAMS name);
}