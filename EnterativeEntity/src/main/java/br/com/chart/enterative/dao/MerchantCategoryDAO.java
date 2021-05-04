package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.MerchantCategory;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class MerchantCategoryDAO extends UserAwareDAO<MerchantCategory, Long> {
    
    public MerchantCategoryDAO(BaseRepository<MerchantCategory, Long> repository) {
        super(repository);
    }
    
}
