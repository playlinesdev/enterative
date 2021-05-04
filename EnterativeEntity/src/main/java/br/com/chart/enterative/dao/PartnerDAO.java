package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Partner;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PartnerDAO extends UserAwareDAO<Partner, Long> {

    public PartnerDAO(BaseRepository<Partner, Long> repository) {
        super(repository);
    }

}
