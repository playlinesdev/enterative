package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.BHNVoucher;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNVoucherDAO extends UserAwareDAO<BHNVoucher, Long> {
    
    public BHNVoucherDAO(BaseRepository<BHNVoucher, Long> repository) {
        super(repository);
    }
    
}
