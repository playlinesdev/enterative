package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;

import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface MerchantRepository extends UserAwareRepository<Merchant, Long> {

    public Merchant findByMerchantIdentifier(String merchantIdentifier);

    public List<Merchant> findByStatusOrderByName(STATUS status);
}
