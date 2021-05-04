package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.CieloShopException;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface CieloShopExceptionRepository extends UserAwareRepository<CieloShopException, Long> {
    
    public List<CieloShopException> findByShopId(Long id);
}
