package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.enums.FILE_TYPE;

import java.util.List;

public interface FileUploadRepository extends UserAwareRepository<FileUpload, Long> {
    List<FileUpload> findByObjectIDAndType(Long objectID, FILE_TYPE type);
}