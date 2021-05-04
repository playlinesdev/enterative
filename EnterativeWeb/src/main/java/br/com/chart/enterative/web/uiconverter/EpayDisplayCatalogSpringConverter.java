package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.vo.epay.EpayDisplayCatalog;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class EpayDisplayCatalogSpringConverter implements Converter<String, EpayDisplayCatalog> {

    @Override
    public EpayDisplayCatalog convert(String source) {
        if (Objects.nonNull(source)) {
            EpayDisplayCatalog result = new EpayDisplayCatalog();
            String[] split = source.split("\\|");
            result.setDisplayGroup(split[0]);
            result.setProductType(split[1]);
            return result;
        } else {
            return null;
        }
    }
}