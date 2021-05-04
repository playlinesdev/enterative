package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ShoppingCartLine;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartLineDAO extends UserAwareDAO<ShoppingCartLine, Long> {
    
    public ShoppingCartLineDAO(BaseRepository<ShoppingCartLine, Long> repository) {
        super(repository);
    }
    
}
