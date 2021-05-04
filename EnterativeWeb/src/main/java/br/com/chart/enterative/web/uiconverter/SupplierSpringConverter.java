package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class SupplierSpringConverter extends BaseSpringConverter<SupplierVO> {

    @Override
    protected Supplier<SupplierVO> getSupplier() {
        return SupplierVO::new;
    }
}
