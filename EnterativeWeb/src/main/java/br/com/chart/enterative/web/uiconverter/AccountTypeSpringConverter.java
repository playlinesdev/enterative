package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountTypeSpringConverter extends BaseSpringConverter<AccountTypeVO> {

    @Override
    protected Supplier<AccountTypeVO> getSupplier() {
        return AccountTypeVO::new;
    }

}
