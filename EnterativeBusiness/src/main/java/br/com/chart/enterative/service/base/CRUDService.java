package br.com.chart.enterative.service.base;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.BaseDAO;
import br.com.chart.enterative.entity.base.BaseEntity;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.base.BaseVO;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;

/**
 *
 * @author William Leite
 * @param <E>
 * @param <PK>
 * @param <V>
 * @param <S>
 */
public abstract class CRUDService<E extends BaseEntity, PK extends Serializable, V extends BaseVO, S extends BaseVO> extends UserAwareComponent {

    @Autowired
    protected EnterativeReflectionUtils reflectionUtils;

    private final BaseDAO<E, PK> dao;
    private final ConverterService<E, V> converter;

    public CRUDService(BaseDAO<E, PK> dao, ConverterService<E, V> converter) {
        this.converter = converter;
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
    public <C extends ConverterService<E, V>> C converter() {
        return (C) this.converter;
    }

    @SuppressWarnings("unchecked")
    public <D extends BaseDAO<E, PK>> D dao() {
        return (D) this.dao;
    }

    protected abstract Supplier<E> initEntitySupplier();

    public E initEntity() {
        return this.initEntitySupplier().get();
    }

    protected abstract Supplier<V> initVOSupplier();

    public V initVO() {
        return this.initVOSupplier().get();
    }

    public List<E> findAll(Sort sort) {
        return this.dao.findAll(sort);
    }

    public Stream<E> findAll() {
        return this.dao.findAll();
    }

    public Stream<V> findAllVO() {
        return this.dao.findAll().map(this.converter::convert);
    }

    public Stream<V> findAllVOSorted(Comparator<V> comparator) {
        return this.findAllVO().sorted(comparator);
    }

    public <E extends BaseEntity> E findOneOrReturnNull(E exampleObject, String[] properties) throws IllegalAccessException, InstantiationException {
        ExampleMatcher matcher = ExampleMatcher.matching();
        for(int i = 0; i< properties.length;i++){
            matcher.withMatcher(properties[i], ExampleMatcher.GenericPropertyMatchers.exact());
        }
        Example<E> example = Example.<E>of(exampleObject, matcher);
        BaseDAO<E, PK> dynamicDao = (BaseDAO<E, PK>) this.dao();
        if (!dynamicDao.repository().exists(example))
            return null;
        return dynamicDao.repository().findOne(example).get();
    }

    public E findOne(PK id) {
        return this.dao.findOne(id);
    }

    public V findOneVO(PK id) {
        return this.converter.convert(this.dao.findOne(id));
    }

    public E saveAndFlush(E entity) {
        return this.dao.saveAndFlush(entity);
    }

    public List<E> saveAndFlush(List<E> entities) {
        return entities.stream().map(this::saveAndFlush).collect(Collectors.toList());
    }

    public void delete(E entity) {
        this.dao.delete(entity);
    }

    public abstract ServiceResponse validate(V vo, CRUD_OPERATION operation);
}
