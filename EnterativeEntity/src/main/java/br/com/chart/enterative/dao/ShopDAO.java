package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.repository.ShopRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopDAO extends UserAwareDAO<Shop, Long> {

    public ShopDAO(BaseRepository<Shop, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopRepository repository() {
        return (ShopRepository) super.repository();
    }

    public Shop findByCode(String code) {
        return this.repository().findByCode(code);
    }
    
    public List<Shop> findByStatusOrderByName(STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }
    
    public Shop findByAccountId(Long id) {
        return this.repository().findByAccountId(id);
    }

    public Page<Shop> findByNameIgnoreCaseContainingAndCodeIgnoreCaseContainingAndStatusOrderByName(String name, String code, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndCodeIgnoreCaseContainingAndStatusOrderByName(name, code, status, pageable);
    }

    public Page<Shop> findByNameIgnoreCaseContainingAndStatusOrderByName(String name, STATUS status, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndStatusOrderByName(name, status, pageable);
    }

    public Page<Shop> findByCodeIgnoreCaseContainingAndStatusOrderByName(String code, STATUS status, Pageable pageable) {
        return this.repository().findByCodeIgnoreCaseContainingAndStatusOrderByName(code, status, pageable);
    }

    public Page<Shop> findByStatusOrderByName(STATUS status, Pageable pageable) {
        return this.repository().findByStatusOrderByName(status, pageable);
    }
}