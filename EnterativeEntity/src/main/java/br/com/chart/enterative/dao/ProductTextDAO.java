package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ProductText;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import br.com.chart.enterative.repository.ProductTextRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductTextDAO extends UserAwareDAO<ProductText, Long> {
    
    public ProductTextDAO(BaseRepository<ProductText, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProductTextRepository repository() {
        return (ProductTextRepository) super.repository();
    }
    
    public List<ProductText> findByType(PRODUCT_TEXT_TYPE type) {
        return this.repository().findByType(type);
    }
    
}
