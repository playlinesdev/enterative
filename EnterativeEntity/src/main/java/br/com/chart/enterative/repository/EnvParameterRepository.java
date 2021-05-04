package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.EnvParameter;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;

/**
 *
 * @author William Leite
 */
public interface EnvParameterRepository extends UserAwareRepository<EnvParameter, Long> {

    public EnvParameter findByParam(ENVIRONMENT_PARAMETER name);
}
