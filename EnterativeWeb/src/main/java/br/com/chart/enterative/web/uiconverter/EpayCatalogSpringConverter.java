package br.com.chart.enterative.web.uiconverter;

import br.com.chart.enterative.vo.epay.EpayCatalog;
import java.util.Objects;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class EpayCatalogSpringConverter implements Converter<String, EpayCatalog>  {

    @Override
    public EpayCatalog convert(String source) {
        if (Objects.nonNull(source) && !source.isEmpty()) {
            EpayCatalog entity = new EpayCatalog();
            entity.setProductId(source);
            return entity;
        } else {
            return null;
        }
    }
}