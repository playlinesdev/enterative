package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SupplierDAO extends UserAwareDAO<Supplier, Long> {
    
    public SupplierDAO(BaseRepository<Supplier, Long> repository) {
        super(repository);
    }
    
}
