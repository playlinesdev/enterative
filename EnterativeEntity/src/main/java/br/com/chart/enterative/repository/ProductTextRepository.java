package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.ProductText;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface ProductTextRepository extends UserAwareRepository<ProductText, Long> {
    
    public List<ProductText> findByType(PRODUCT_TEXT_TYPE type);
}
