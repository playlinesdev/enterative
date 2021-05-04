package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.web.uiconverter.base.BaseSpringConverter;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class ServerSpringConverter extends BaseSpringConverter<ServerVO> {

    @Override
    protected Supplier<ServerVO> getSupplier() {
        return ServerVO::new;
    }

}
