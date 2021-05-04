package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.EpayVoucher;
import br.com.chart.enterative.repository.EpayVoucherRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayVoucherDAO extends UserAwareDAO<EpayVoucher, Long> {

    public EpayVoucherDAO(BaseRepository<EpayVoucher, Long> repository) {
        super(repository);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public EpayVoucherRepository repository() {
        return (EpayVoucherRepository) super.repository();
    }

}
