package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.repository.BHNActivationRepository;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class BHNActivationDAO extends UserAwareDAO<BHNActivation, Long> {

    public BHNActivationDAO(BaseRepository<BHNActivation, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BHNActivationRepository repository() {
        return (BHNActivationRepository) super.repository();
    }
    
    public Page<BHNActivation> findByCreatedAtGreaterThan(Date date, Pageable pageable) {
        return this.repository().findByCreatedAtGreaterThan(date, pageable);
    }
    
    public Page<BHNActivation> findByCreatedAtLessThan(Date date, Pageable pageable) {
        return this.repository().findByCreatedAtLessThan(date, pageable);
    }
    
    public Page<BHNActivation> findByCreatedAtBetween(Date start, Date end, Pageable pageable) {
        return this.repository().findByCreatedAtBetween(start, end, pageable);
    }
    
    public BHNActivation findByExternalCode(String code) {
        return this.repository().findByExternalCode(code);
    }
    
    public List<BHNActivation> findByShopCode(String code) {
        return this.repository().findByShopCode(code);
    }
    
    public Page<BHNActivation> findByExternalCode(String externalCode, Pageable pageable) {
        return this.repository().findByExternalCode(externalCode, pageable);
    }
    
    public Page<BHNActivation> findById(Long id, Pageable pageable) {
        return this.repository().findById(id, pageable);
    }

    public List<BHNActivation> findByStatusAndQueueStatus(ACTIVATION_STATUS status, ACTIVATION_QUEUE_STATUS queueStatus) {
        return this.repository().findByStatusAndQueueStatus(status, queueStatus);
    }

    public BHNActivation findByMerchantAndShopCodeAndTerminalAndExternalCode(Merchant merchant, String shopCode, String terminal, String externalCode) {
        return this.repository().findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, externalCode);
    }

    public void setCallbackStatusForID(CALLBACK_STATUS status, Long id) {
        this.repository().setCallbackStatusForID(status, id);
    }
}
