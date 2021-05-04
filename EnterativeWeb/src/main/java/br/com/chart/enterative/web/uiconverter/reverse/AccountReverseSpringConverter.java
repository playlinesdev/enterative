package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountReverseSpringConverter extends BaseReverseSpringConverter<AccountVO> {

    @Override
    protected Supplier<AccountVO> getSupplier() {
        return AccountVO::new;
    }

}
