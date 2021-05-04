package br.com.chart.enterative.service.base;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.base.NamedVO;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 * @param <E> Entity
 * @param <PK> PK
 * @param <V> VO
 * @param <S>
 */
public abstract class UserAwareCRUDService<E extends UserAwareEntity, PK extends Serializable, V extends UserAwareVO, S extends NamedVO> extends CRUDService<E, PK, V, S> {

    public UserAwareCRUDService(UserAwareDAO<E, PK> dao, ConverterService<E, V> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserAwareDAO<E, PK> dao() {
        return super.dao();
    }

    public Page<E> findByNameIgnoreCaseContaining(String name, Pageable pageable) {
        return this.dao().findByNameIgnoreCaseContaining(name, pageable);
    }

    public PageWrapper<V> retrieve(S searchForm, Pageable pageable, String url) {
        Page<E> page;

        if (Objects.nonNull(searchForm.getName())) {
            page = this.dao().findByNameIgnoreCaseContainingOrderByName(searchForm.getName(), pageable);
        } else {
            page = this.dao().findAll(pageable);
        }

        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    @SuppressWarnings("unchecked")
    public void fill(E entity) {
        if (Objects.nonNull(entity.getId())) {
            E db = this.dao().findOne((PK) entity.getId());
            entity.setCreatedAt(db.getCreatedAt());
            entity.setCreatedBy(db.getCreatedBy());
        }
    }

    @SuppressWarnings("unchecked")
    public void fill(V vo) {
        if (Objects.nonNull(vo.getId())) {
            E db = this.dao().findOne((PK) vo.getId());
            vo.setCreatedAt(db.getCreatedAt());
            vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, db.getCreatedBy()));
        }
    }

    @Override
    @Deprecated
    public E saveAndFlush(E entity) {
        throw new UnsupportedOperationException();
    }

    public E saveAndFlush(E entity, Long userID) {
        return this.dao().saveAndFlush(entity, userID);
    }

    public List<E> saveAndFlush(List<E> entities, final Long userID) {
        return entities.stream().map(e -> this.saveAndFlush(e, userID)).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = {CRUDServiceException.class})
    public ServiceResponse processSave(V vo, Long user) throws CRUDServiceException {
        E entity = this.converter().convert(vo);
        entity = this.processSave(entity, user).get("entity");
        return new ServiceResponse().put("entity", this.converter().convert(entity));
    }

    @Transactional(rollbackFor = {CRUDServiceException.class})
    public ServiceResponse processSave(E entity, Long user) throws CRUDServiceException {
        this.fill(entity);
        entity = this.dao().saveAndFlush(entity, user);
        return new ServiceResponse().put("entity", entity);
    }
}
