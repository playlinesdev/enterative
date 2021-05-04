package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.FILE_TYPE;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadVO extends UserAwareVO {
    private FILE_TYPE type;
    private Long objectID;
    private String fileData;
    private MultipartFile file;
    private String objectName;
}