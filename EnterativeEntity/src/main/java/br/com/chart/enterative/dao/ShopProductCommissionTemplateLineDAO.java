package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.ShopProductCommissionTemplateLine;
import br.com.chart.enterative.repository.ShopProductCommissionRepository;
import br.com.chart.enterative.repository.ShopProductCommissionTemplateLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateLineDAO extends UserAwareDAO<ShopProductCommissionTemplateLine, Long> {

    public ShopProductCommissionTemplateLineDAO(BaseRepository<ShopProductCommissionTemplateLine, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionTemplateLineRepository repository() {
        return (ShopProductCommissionTemplateLineRepository) super.repository();
    }
}