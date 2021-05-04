package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ShoppingCart;
import br.com.chart.enterative.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartDAO extends UserAwareDAO<ShoppingCart, Long> {

    public ShoppingCartDAO(BaseRepository<ShoppingCart, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShoppingCartRepository repository() {
        return (ShoppingCartRepository) super.repository();
    }

    public ShoppingCart findByCreatedById(Long userID) {
        return this.repository().findByCreatedById(userID);
    }

}
