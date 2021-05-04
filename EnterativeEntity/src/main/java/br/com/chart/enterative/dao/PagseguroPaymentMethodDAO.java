package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.PagseguroPaymentMethod;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.PagseguroPaymentMethodRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PagseguroPaymentMethodDAO extends UserAwareDAO<PagseguroPaymentMethod, Long> {
    
    public PagseguroPaymentMethodDAO(BaseRepository<PagseguroPaymentMethod, Long> repository) {
        super(repository);
    }
    
     @Override
    @SuppressWarnings("unchecked")
    public PagseguroPaymentMethodRepository repository() {
        return (PagseguroPaymentMethodRepository) super.repository();
    }
    
    public List<PagseguroPaymentMethod> findByStatus(STATUS status) {
        return this.repository().findByStatus(status);
    }
    
}
