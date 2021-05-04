package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface EpayActivationRepository extends UserAwareRepository<EpayActivation, Long> {
   
    public List<EpayActivation> findByStatusInAndQueueStatus(ACTIVATION_STATUS[] status, ACTIVATION_QUEUE_STATUS queueStatus);
    
    public EpayActivation findByMerchantAndShopCodeAndTerminalAndExternalCode(Merchant merchant, String shopCode, String terminal, String externalCode);
    
    @Modifying
    @Transactional
    @Query("UPDATE EpayActivation SET callbackStatus = :status WHERE id = :id")
    public void setCallbackStatusForID(@Param("status") CALLBACK_STATUS status, @Param("id") Long id);
}