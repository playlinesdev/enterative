package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class SupplierReverseSpringConverter extends BaseReverseSpringConverter<SupplierVO>{

    @Override
    protected Supplier<SupplierVO> getSupplier() {
        return SupplierVO::new;
    }

}
