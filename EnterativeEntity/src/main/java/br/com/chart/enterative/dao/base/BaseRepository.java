package br.com.chart.enterative.dao.base;

import br.com.chart.enterative.entity.base.BaseEntity;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author William Leite
 * @param <E> Entity
 * @param <PK> PK
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity, PK extends Serializable> extends JpaRepository<E, PK> {

    <T> T findById(PK id, Class<T> projection);
}
