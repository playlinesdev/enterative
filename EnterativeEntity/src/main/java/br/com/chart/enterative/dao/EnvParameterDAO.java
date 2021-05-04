package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.EnvParameter;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.repository.EnvParameterRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EnvParameterDAO extends UserAwareDAO<EnvParameter, Long> {

    public EnvParameterDAO(BaseRepository<EnvParameter, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EnvParameterRepository repository() {
        return (EnvParameterRepository) super.repository();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ENVIRONMENT_PARAMETER parameter) {
        EnvParameter env = this.repository().findByParam(parameter);
        String value = String.valueOf(parameter.getDefaultValue());
        Class<?> clazz = parameter.getClazz();

        if (Objects.nonNull(env)) {
            value = env.getValue();
        }

        if (clazz == String.class) {
            return (T) value;
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(value);
        }
        return null;
    }

    public <T> void set(ENVIRONMENT_PARAMETER parameter, T value, Long user) {
        EnvParameter env = this.repository().findByParam(parameter);
        if (Objects.isNull(env)) {
            env = new EnvParameter();
            env.setParam(parameter);
            env.setName(parameter.getName());
        }
        env.setValue(String.valueOf(value));
        this.saveAndFlush(env, user);
    }

    public void initializeEnvVariables() {
        //Max units sold by product parameter initializer
        if(this.get(ENVIRONMENT_PARAMETER.MAX_ITEM_PURCHASE_UNITS) == null){
            this.set(ENVIRONMENT_PARAMETER.MAX_ITEM_PURCHASE_UNITS,50.0f,get(ENVIRONMENT_PARAMETER.SYSTEM_USER));
        }
    }
}
