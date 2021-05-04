package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.BHNTransactionVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class BHNTransactionSpringConverter extends BaseSpringConverter<BHNTransactionVO> {

    @Override
    protected Supplier<BHNTransactionVO> getSupplier() {
        return BHNTransactionVO::new;
    }

}
