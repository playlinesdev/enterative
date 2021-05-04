package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.web.uiconverter.base.BaseReverseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ServerReverseSpringConverter extends BaseReverseSpringConverter<ServerVO> {

    @Override
    protected Supplier<ServerVO> getSupplier() {
        return ServerVO::new;
    }

}
