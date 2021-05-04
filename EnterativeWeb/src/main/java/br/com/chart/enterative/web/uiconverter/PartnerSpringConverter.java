package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class PartnerSpringConverter extends BaseSpringConverter<PartnerVO> {

    @Override
    protected Supplier<PartnerVO> getSupplier() {
        return PartnerVO::new;
    }

}
