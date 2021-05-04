package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountSpringConverter extends BaseSpringConverter<AccountVO> {

    @Override
    protected Supplier<AccountVO> getSupplier() {
        return AccountVO::new;
    }

}
