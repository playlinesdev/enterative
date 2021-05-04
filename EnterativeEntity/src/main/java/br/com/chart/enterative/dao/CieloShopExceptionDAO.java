package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.CieloShopException;
import br.com.chart.enterative.repository.CieloShopExceptionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CieloShopExceptionDAO extends UserAwareDAO<CieloShopException, Long> {
    
    public CieloShopExceptionDAO(BaseRepository<CieloShopException, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CieloShopExceptionRepository repository() {
        return (CieloShopExceptionRepository) super.repository();
    }
    
    public List<CieloShopException> findByShopId(Long id) {
        return this.repository().findByShopId(id);
    }
    
}
