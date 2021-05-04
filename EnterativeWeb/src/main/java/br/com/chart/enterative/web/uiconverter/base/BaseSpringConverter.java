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
public abstract class BaseSpringConverter<T extends NamedVO> implements Converter<String, T> {

    protected abstract Supplier<T> getSupplier();

    @Override
    public T convert(String source) {
        if (Objects.nonNull(source) && !source.isEmpty()) {
            T entity = this.getSupplier().get();
            entity.setId(Long.valueOf(source));
            return entity;
        } else {
            return null;
        }
    }

}
