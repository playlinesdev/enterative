package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.ShoppingCart;

/**
 *
 * @author William Leite
 */
public interface ShoppingCartRepository extends UserAwareRepository<ShoppingCart, Long> {

    public ShoppingCart findByCreatedById(Long userID);
}
