package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface ShopProductCommissionRepository extends UserAwareRepository<ShopProductCommission, Long> {

    public ShopProductCommission findByShopAndProduct(Shop shop, Product product);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShopProductCommission WHERE id IN :id")
    public void deleteByID(@Param("id") List<Long> id);

    public List<ShopProductCommission> findByProductId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShopProductCommission WHERE product.id = :id")
    public void deleteByProductId(@Param("id") Long id);
}
