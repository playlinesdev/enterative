package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class PartnerReverseSpringConverter extends BaseReverseSpringConverter<PartnerVO> {

    @Override
    protected Supplier<PartnerVO> getSupplier() {
        return PartnerVO::new;
    }

}
