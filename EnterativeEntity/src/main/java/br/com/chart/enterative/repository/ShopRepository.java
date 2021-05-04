package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 */
public interface ShopRepository extends UserAwareRepository<Shop, Long> {

    @Query("SELECT s FROM Shop s LEFT JOIN FETCH s.commissions WHERE s.id = :id")
    public Shop findByIdWithCommissionsEager(@Param("id") Long id);

    public List<Shop> findByStatusOrderByName(STATUS status);
    
    public Shop findByAccountId(Long id);

    public Page<Shop> findByNameIgnoreCaseContainingAndCodeIgnoreCaseContainingAndStatusOrderByName(String name, String code, STATUS status, Pageable pageable);

    public Page<Shop> findByNameIgnoreCaseContainingAndStatusOrderByName(String name, STATUS status, Pageable pageable);

    public Page<Shop> findByCodeIgnoreCaseContainingAndStatusOrderByName(String code, STATUS status, Pageable pageable);

    public Page<Shop> findByStatusOrderByName(STATUS status, Pageable pageable);
    
    public Shop findByCode(String code);
}
