package br.com.chart.enterative.web.uiconverter.reverse;

import br.com.chart.enterative.vo.epay.EpayDisplayCatalog;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class EpayDisplayCatalogReverseSpringConverter implements Converter<EpayDisplayCatalog, String> {

    @Override
    public String convert(EpayDisplayCatalog source) {
        if (Objects.nonNull(source)) {
            return String.format("%s|%s", source.getDisplayGroup(), source.getProductType());
        } else {
            return null;
        }
    }
}
