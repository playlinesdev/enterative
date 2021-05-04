package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.repository.EpayActivationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayActivationDAO extends UserAwareDAO<EpayActivation, Long> {

    public EpayActivationDAO(BaseRepository<EpayActivation, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EpayActivationRepository repository() {
        return (EpayActivationRepository) super.repository();
    }
    
    public List<EpayActivation> findByStatusInAndQueueStatus(ACTIVATION_STATUS[] status, ACTIVATION_QUEUE_STATUS queueStatus) {
        return this.repository().findByStatusInAndQueueStatus(status, queueStatus);
    }
    
    public EpayActivation findByMerchantAndShopCodeAndTerminalAndExternalCode(Merchant merchant, String shopCode, String terminal, String externalCode) {
        return this.repository().findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, externalCode);
    }
    
    public void setCallbackStatusForID(CALLBACK_STATUS status, Long id) {
        this.repository().setCallbackStatusForID(status, id);
    }
}
