package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductHighlight;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductHighlightDAO extends UserAwareDAO<ProductHighlight, Long> {
    
    public ProductHighlightDAO(BaseRepository<ProductHighlight, Long> repository) {
        super(repository);
    }
    
}
