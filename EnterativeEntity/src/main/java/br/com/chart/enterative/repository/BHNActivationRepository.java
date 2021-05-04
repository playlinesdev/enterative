package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface BHNActivationRepository extends UserAwareRepository<BHNActivation, Long> {

    public List<BHNActivation> findByStatusAndQueueStatus(ACTIVATION_STATUS status, ACTIVATION_QUEUE_STATUS queueStatus);

    public BHNActivation findByMerchantAndShopCodeAndTerminalAndExternalCode(Merchant merchant, String shopCode, String terminal, String externalCode);
    
    public Page<BHNActivation> findByExternalCode(String externalCode, Pageable pageable);
    
    public Page<BHNActivation> findById(Long id, Pageable pageable);
    
    public BHNActivation findByExternalCode(String code);
    
    public Page<BHNActivation> findByCreatedAtGreaterThan(Date date, Pageable pageable);
    
    public Page<BHNActivation> findByCreatedAtLessThan(Date date, Pageable pageable);
    
    public Page<BHNActivation> findByCreatedAtBetween(Date start, Date end, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE BHNActivation SET callbackStatus = :status WHERE id = :id")
    public void setCallbackStatusForID(@Param("status") CALLBACK_STATUS status, @Param("id") Long id);
    
    public List<BHNActivation> findByShopCode(String code);
}
