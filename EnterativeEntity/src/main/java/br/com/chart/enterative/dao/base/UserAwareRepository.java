package br.com.chart.enterative.dao.base;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author William Leite
 * @param <E> Entity
 * @param <PK> PK
 */
@NoRepositoryBean
public interface UserAwareRepository<E extends UserAwareEntity, PK extends Serializable> extends BaseRepository<E, PK> {

    @Query("SELECT t FROM #{#entityName} t WHERE t.name LIKE CONCAT('%',:name,'%') OR t.name IS NULL ORDER BY t.name")
    public Page<E> findByNameIgnoreCaseContainingOrderByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT t FROM #{#entityName} t WHERE t.name LIKE CONCAT('%',:name,'%') OR t.name IS NULL")
    public Page<E> findByNameIgnoreCaseContaining(@Param("name") String name, Pageable pageable);

    public E findByName(String name);
}
