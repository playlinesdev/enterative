package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.FILE_TYPE;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "file_upload")
@Getter
@Setter
public class FileUpload extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated
    @Column(name = "type")
    private FILE_TYPE type;

    @Column(name = "object_id")
    private Long objectID;

    @Column(name = "file_data", columnDefinition = "LONGTEXT")
    private String fileData;
}
