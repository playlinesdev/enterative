package br.com.chart.enterative.web.uiconverter.base;

import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author William Leite
 * @param <T>
 */
public abstract class BaseReverseSpringConverter<T extends NamedVO> implements Converter<T, String> {

    protected abstract Supplier<T> getSupplier();

    @Override
    public String convert(T source) {
        if (Objects.nonNull(source)) {
            return source.getId().toString();
        } else {
            return null;
        }
    }

}
