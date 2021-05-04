package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.MerchantRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class MerchantDAO extends UserAwareDAO<Merchant, Long> {

    public MerchantDAO(BaseRepository<Merchant, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MerchantRepository repository() {
        return (MerchantRepository) super.repository();
    }

    public List<Merchant> findByStatusOrderByName(STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }

    public Merchant findByMerchantIdentifier(String merchantIdentifier) {
        return this.repository().findByMerchantIdentifier(merchantIdentifier);
    }

}
