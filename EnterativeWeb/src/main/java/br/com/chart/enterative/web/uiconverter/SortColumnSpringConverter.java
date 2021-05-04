package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.vo.SortColumnVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class SortColumnSpringConverter extends BaseSpringConverter<SortColumnVO> {

    @Override
    protected Supplier<SortColumnVO> getSupplier() {
        return SortColumnVO::new;
    }
}
