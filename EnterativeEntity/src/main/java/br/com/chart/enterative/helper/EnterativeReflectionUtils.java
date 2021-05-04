package br.com.chart.enterative.helper;

import br.com.chart.enterative.entity.base.BaseEntity;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.vo.base.BaseVO;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author William Leite
 */
@Service
public class EnterativeReflectionUtils {

    @SuppressWarnings("rawtypes")
    public String convert(ConstraintViolationException ex) {
        final Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().stream().forEach(c -> {
            ConstraintViolationImpl impl = (ConstraintViolationImpl) c;
            violations.put(impl.getPropertyPath().toString(), impl.getMessage());
        });
        return violations.entrySet().stream().map(es -> {
            String field = es.getKey();
            String violation = es.getValue();
            return String.format("%s - %s", field, violation);
        }).collect(Collectors.joining(ENTITY_MESSAGE.LINE_SEPARATOR));
    }

    private String assembleMethod(String prefix, String method) {
        return String.format("%s%s", prefix, method);
    }

    private String assembleGet(String property) {
        char upper = property.toUpperCase().charAt(0);
        String rest = property.substring(1);
        return this.assembleMethod("get", String.format("%s%s", upper, rest));
    }

    private String assembleSet(String property) {
        char upper = property.toUpperCase().charAt(0);
        String rest = property.substring(1);
        return this.assembleMethod("set", String.format("%s%s", upper, rest));
    }

    @SuppressWarnings("unchecked")
    public <R, T> R invokeGetter(T obj, String property) {
        try {
            String methodName = this.assembleGet(property);
            Method method = ReflectionUtils.findMethod(obj.getClass(), methodName);
            return (R) ReflectionUtils.invokeMethod(method, obj);
        } catch (IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T, P> T invokeSetter(T obj, String property, P value) {
        try {
            String methodName = this.assembleSet(property);
            Method method;
            if (Objects.nonNull(value)) {
                method = ReflectionUtils.findMethod(obj.getClass(), methodName, value.getClass());
            } else {
                method = ReflectionUtils.findMethod(obj.getClass(), methodName, new Class[]{null});
            }
            ReflectionUtils.invokeMethod(method, obj, value);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends UserAwareEntity, V extends UserAwareVO> T asHollowLink(Supplier<T> supplier, V vo) {
        if (Objects.nonNull(vo)) {
            T entity = supplier.get();
            entity.setId(vo.getId());
            return entity;
        }
        return null;
    }

    public <V extends UserAwareVO> V createHollowLink(Supplier<V> supplier, Long value) {
        if (Objects.nonNull(value)) {
            V vo = supplier.get();
            vo.setId(value);
            return vo;
        }
        return null;
    }

    public <T extends UserAwareEntity> T asHollowLink(Supplier<T> supplier, Long value) {
        if (Objects.nonNull(value)) {
            T entity = supplier.get();
            entity.setId(value);
            return entity;
        }
        return null;
    }

    public <V extends UserAwareVO, T extends UserAwareEntity> V asHollowLink(Supplier<V> supplier, T entity) {
        if (Objects.nonNull(entity)) {
            V vo = supplier.get();
            vo.setId(entity.getId());
            return vo;
        }
        return null;
    }

    public <V extends BaseVO, T extends BaseEntity> V asNamedLink(Supplier<V> supplier, T entity) {
        if (Objects.nonNull(entity)) {
            V vo = supplier.get();
            vo = this.invokeSetter(vo, "id", this.invokeGetter(entity, "id"));
            vo = this.invokeSetter(vo, "name", this.invokeGetter(entity, "name"));
            return vo;
        }
        return null;
    }
}
