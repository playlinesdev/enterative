package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountTypeReverseSpringConverter extends BaseReverseSpringConverter<AccountTypeVO> {

    @Override
    protected Supplier<AccountTypeVO> getSupplier() {
        return AccountTypeVO::new;
    }

}
