package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.*;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import br.com.chart.enterative.projections.ProductFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author William Leite
 */
@Service
public class FileUploadConverterService extends ConverterService<FileUpload, FileUploadVO> {

    @Autowired
    private ProductDAO productDAO;

    public FileUploadConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public FileUpload convert(FileUploadVO vo) {
        FileUpload entity = new FileUpload();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        if (Objects.nonNull(vo.getFile())) {
            try {
                entity.setFileData(Base64.getEncoder().encodeToString(vo.getFile().getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            entity.setFileData(vo.getFileData());
        }

        entity.setObjectID(vo.getObjectID());
        entity.setType(vo.getType());
        return entity;
    }

    @Override
    public FileUploadVO convert(FileUpload entity) {
        FileUploadVO vo = new FileUploadVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setFileData(entity.getFileData());
        vo.setObjectID(entity.getObjectID());
        vo.setType(entity.getType());

        switch (vo.getType()) {
            case PRODUCT_IMAGE:
                ProductFileUpload product = this.productDAO.repository().findById(entity.getObjectID(), ProductFileUpload.class);
                vo.setObjectName(String.format("[%s] %s", product.getId(), product.getName()));
                break;
        }
        return vo;
    }
}
