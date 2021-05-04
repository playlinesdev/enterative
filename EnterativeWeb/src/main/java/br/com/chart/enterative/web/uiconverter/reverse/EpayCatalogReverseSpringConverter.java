package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.vo.epay.EpayCatalog;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class EpayCatalogReverseSpringConverter implements Converter<EpayCatalog, String>  {

    @Override
    public String convert(EpayCatalog source) {
        if (Objects.nonNull(source)) {
            return source.getProductId();
        } else {
            return null;
        }
    }
}