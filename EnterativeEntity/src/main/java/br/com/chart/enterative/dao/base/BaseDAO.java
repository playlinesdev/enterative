package br.com.chart.enterative.dao.base;

import br.com.chart.enterative.entity.base.BaseEntity;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 * @param <E>
 * @param <PK>
 */
@Transactional
public abstract class BaseDAO<E extends BaseEntity, PK extends Serializable> {

    @PersistenceContext
    private EntityManager entityManager;

    private final BaseRepository<E, PK> repository;

    public BaseDAO(BaseRepository<E, PK> repository) {
        this.repository = repository;
    }

    @SuppressWarnings("unchecked")
    public <R extends BaseRepository<E, PK>> R repository() {
        return (R) this.repository;
    }

    public void refresh(E entity) {
        this.entityManager.refresh(entity);
    }

    public Stream<E> findAll() {
        return this.repository.findAll().stream();
    }

    public List<E> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    public Page<E> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public E findOne(PK id) {
        return this.repository.getOne(id);
    }

    public void delete(E entity) {
        this.repository.delete(entity);
    }

    public E saveAndFlush(E entity) {
        E e = this.repository.saveAndFlush(entity);
        this.refresh(e);
        return e;
    }

    public void deleteById(PK id) {
        this.repository.deleteById(id);
    }
}
