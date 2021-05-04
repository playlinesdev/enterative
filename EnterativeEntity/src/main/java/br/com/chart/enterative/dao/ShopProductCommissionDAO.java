package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.repository.ShopProductCommissionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionDAO extends UserAwareDAO<ShopProductCommission, Long> {

    public ShopProductCommissionDAO(BaseRepository<ShopProductCommission, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionRepository repository() {
        return (ShopProductCommissionRepository) super.repository();
    }

    public ShopProductCommission findByShopAndProduct(Shop shop, Product product) {
        return this.repository().findByShopAndProduct(shop, product);
    }

    public List<ShopProductCommission> findByProductId(Long id) {
        return this.repository().findByProductId(id);
    }

}
