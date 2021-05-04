package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.SDFFile;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.repository.SDFFileRepository;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SDFFileDAO extends UserAwareDAO<SDFFile, Long> {

    public SDFFileDAO(BaseRepository<SDFFile, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SDFFileRepository repository() {
        return (SDFFileRepository) super.repository();
    }

    public List<SDFFile> findByStatusOrderByName(SDF_FILE_STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }

    public List<SDFFile> findByCreatedAtBetweenOrderByName(Date begin, Date end) {
        return this.repository().findByCreatedAtBetweenOrderByName(begin, end);
    }

    public List<SDFFile> findByCreatedAtBetweenAndStatusOrderByName(Date begin, Date end, SDF_FILE_STATUS status) {
        return this.repository().findByCreatedAtBetweenAndStatusOrderByName(begin, end, status);
    }

    public List<SDFFile> findByNameContainingIgnoreCaseOrderByName(String name) {
        return this.repository().findByNameContainingIgnoreCaseOrderByName(name);
    }

    public List<SDFFile> findByNameContainingIgnoreCaseAndStatusOrderByName(String name, SDF_FILE_STATUS status) {
        return this.repository().findByNameContainingIgnoreCaseAndStatusOrderByName(name, status);
    }

    public List<SDFFile> findByNameContainingIgnoreCaseAndCreatedAtBetweenOrderByName(String name, Date begin, Date end) {
        return this.repository().findByNameContainingIgnoreCaseAndCreatedAtBetweenOrderByName(name, begin, end);
    }

    public List<SDFFile> findByNameContainingIgnoreCaseAndCreatedAtBetweenAndStatusOrderByName(String name, Date begin, Date end, SDF_FILE_STATUS status) {
        return this.repository().findByNameContainingIgnoreCaseAndCreatedAtBetweenAndStatusOrderByName(name, begin, end, status);
    }

    public boolean existsByName(String name) {
        return this.repository().existsByName(name);
    }

    public void setStatusById(SDF_FILE_STATUS status, Long id) {
        this.repository().setStatusById(status, id);
    }
}
