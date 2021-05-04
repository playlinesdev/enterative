package br.com.chart.enterative.dao.base;

import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.vo.ServiceResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 * @param <E>
 * @param <PK>
 */
public abstract class UserAwareDAO<E extends UserAwareEntity, PK extends Serializable> extends BaseDAO<E, PK> {

    @Autowired
    private EnterativeReflectionUtils reflectionUtils;

    public UserAwareDAO(BaseRepository<E, PK> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserAwareRepository<E, PK> repository() {
        return super.repository();
    }

    @Override
    @Deprecated
    public E saveAndFlush(E entity) {
        throw new UnsupportedOperationException();
    }

    public Page<E> findByNameIgnoreCaseContainingOrderByName(@Param("name") String name, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingOrderByName(name, pageable);
    }

    public Page<E> findByNameIgnoreCaseContaining(@Param("name") String name, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContaining(name, pageable);
    }

    public E findByName(String name) {
        return this.repository().findByName(name);
    }

    public E saveAndFlush(E entity, Long user) throws CRUDServiceException {
        try {
            if (Objects.isNull(entity.getId())) {
                entity.setCreatedAt(new Date());
                entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, user));
            } else {
                entity.setAlteredAt(new Date());
                entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, user));
            }

            E e = this.repository().saveAndFlush(entity);
            this.refresh(e);
            return e;
        } catch (ConstraintViolationException e) {
            ServiceResponse result = new ServiceResponse().setMessage(this.reflectionUtils.convert(e));
            throw new CRUDServiceException(result, true);
        } catch (Exception e) {
            ServiceResponse result = new ServiceResponse().setMessage("Ocorreu um erro: %s", Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
    }
}
