package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductCategory;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductCategoryDAO extends UserAwareDAO<ProductCategory, Long> {
    
    public ProductCategoryDAO(BaseRepository<ProductCategory, Long> repository) {
        super(repository);
    }
    
}
