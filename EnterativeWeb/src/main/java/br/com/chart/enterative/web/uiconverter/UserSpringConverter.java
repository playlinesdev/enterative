package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class UserSpringConverter extends BaseSpringConverter<UserVO> {

    @Override
    protected Supplier<UserVO> getSupplier() {
        return UserVO::new;
    }

}
