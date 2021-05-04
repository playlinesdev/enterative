package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ShopProductCommissionTemplate;
import br.com.chart.enterative.entity.ShopProductCommissionTemplateLine;
import br.com.chart.enterative.repository.ShopProductCommissionTemplateLineRepository;
import br.com.chart.enterative.repository.ShopProductCommissionTemplateRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateDAO extends UserAwareDAO<ShopProductCommissionTemplate, Long> {

    public ShopProductCommissionTemplateDAO(BaseRepository<ShopProductCommissionTemplate, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionTemplateRepository repository() {
        return (ShopProductCommissionTemplateRepository) super.repository();
    }
}