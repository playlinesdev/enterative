package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import br.com.chart.enterative.entity.SDFDetail;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @author William Leite
 */
public interface SDFDetailRepository extends UserAwareRepository<SDFDetail, Long> {

    public SDFDetail findByBhnTransaction(Long bhnTransaction);

    public Page<SDFDetail> findByFileIdOrderByPosTransactionDateAscPosTransactionTimeAsc(Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE SDFDetail SET bhnTransaction = :bhnTransaction, status = :status WHERE id = :id")
    public void setBhnTransactionAndStatusById(@Param("bhnTransaction") Long bhnTransaction, @Param("status") SDF_DETAIL_STATUS status, @Param("id") Long id);

    public List<SDFDetail> findByPosTransactionDateBetweenAndStatus(Date start, Date end, SDF_DETAIL_STATUS status);
}
