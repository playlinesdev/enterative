package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.PagseguroPaymentMethod;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface PagseguroPaymentMethodRepository extends UserAwareRepository<PagseguroPaymentMethod, Long> {

    public List<PagseguroPaymentMethod> findByStatus(STATUS status);
}
