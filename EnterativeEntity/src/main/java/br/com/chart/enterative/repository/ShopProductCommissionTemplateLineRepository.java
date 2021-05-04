package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.ShopProductCommissionTemplateLine;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author William Leite
 */
public interface ShopProductCommissionTemplateLineRepository extends UserAwareRepository<ShopProductCommissionTemplateLine, Long> {
}
