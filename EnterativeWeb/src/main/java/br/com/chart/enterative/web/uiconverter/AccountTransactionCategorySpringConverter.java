package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountTransactionCategorySpringConverter extends BaseSpringConverter<AccountTransactionCategoryVO> {

    @Override
    protected Supplier<AccountTransactionCategoryVO> getSupplier() {
        return AccountTransactionCategoryVO::new;
    }

}
