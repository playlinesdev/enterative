package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.repository.FileUploadRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileUploadDAO extends UserAwareDAO<FileUpload, Long> {

    public FileUploadDAO(BaseRepository<FileUpload, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public FileUploadRepository repository() {
        return (FileUploadRepository) super.repository();
    }

    public Page<FileUpload> findAll(Example<FileUpload> example, Pageable pageable) {
        return this.repository().findAll(example, pageable);
    }

    public List<FileUpload> findByObjectIDAndType(Long objectID, FILE_TYPE type) {
        return this.repository().findByObjectIDAndType(objectID, type);
    }
}
