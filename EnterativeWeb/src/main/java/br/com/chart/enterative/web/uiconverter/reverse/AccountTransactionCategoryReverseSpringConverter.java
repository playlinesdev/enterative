package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class AccountTransactionCategoryReverseSpringConverter extends BaseReverseSpringConverter<AccountTransactionCategoryVO> {

    @Override
    protected Supplier<AccountTransactionCategoryVO> getSupplier() {
        return AccountTransactionCategoryVO::new;
    }

}
