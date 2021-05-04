package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.SDFDetail;
import br.com.chart.enterative.enums.SDF_DETAIL_STATUS;
import br.com.chart.enterative.repository.SDFDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author William Leite
 */
@Service
public class SDFDetailDAO extends UserAwareDAO<SDFDetail, Long> {

    public SDFDetailDAO(BaseRepository<SDFDetail, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SDFDetailRepository repository() {
        return (SDFDetailRepository) super.repository();
    }

    public SDFDetail findByBhnTransaction(Long bhnTransaction) {
        return this.repository().findByBhnTransaction(bhnTransaction);
    }

    public Page<SDFDetail> findByFileIdOrderByPosTransactionDateAscPosTransactionTimeAsc(Long id, Pageable pageable) {
        return this.repository().findByFileIdOrderByPosTransactionDateAscPosTransactionTimeAsc(id, pageable);
    }

    public void setBhnTransactionAndStatusById(Long bhnTransaction, SDF_DETAIL_STATUS status, Long id) {
        this.repository().setBhnTransactionAndStatusById(bhnTransaction, status, id);
    }

    public List<SDFDetail> findByPosTransactionDateBetweenAndStatus(final Date start, final Date end, final SDF_DETAIL_STATUS status) {
        return this.repository().findByPosTransactionDateBetweenAndStatus(start, end, status);
    }
}
