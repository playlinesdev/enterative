package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;

import br.com.chart.enterative.entity.SDFFile;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface SDFFileRepository extends UserAwareRepository<SDFFile, Long> {

    public <S extends SDFFile> List<S> findByStatusOrderByName(SDF_FILE_STATUS status);

    public <S extends SDFFile> List<S> findByCreatedAtBetweenOrderByName(Date begin, Date end);

    public <S extends SDFFile> List<S> findByCreatedAtBetweenAndStatusOrderByName(Date begin, Date end, SDF_FILE_STATUS status);

    public <S extends SDFFile> List<S> findByNameContainingIgnoreCaseOrderByName(String name);

    public <S extends SDFFile> List<S> findByNameContainingIgnoreCaseAndStatusOrderByName(String name, SDF_FILE_STATUS status);

    public <S extends SDFFile> List<S> findByNameContainingIgnoreCaseAndCreatedAtBetweenOrderByName(String name, Date begin, Date end);

    public <S extends SDFFile> List<S> findByNameContainingIgnoreCaseAndCreatedAtBetweenAndStatusOrderByName(String name, Date begin, Date end, SDF_FILE_STATUS status);

    public boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE SDFFile SET status = :status WHERE id = :id")
    public void setStatusById(@Param("status") SDF_FILE_STATUS status, @Param("id") Long id);

}
