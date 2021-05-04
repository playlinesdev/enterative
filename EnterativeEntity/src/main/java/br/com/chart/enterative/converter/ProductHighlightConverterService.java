package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.FileUploadDAO;
import br.com.chart.enterative.dao.ProductHighlightDAO;
import br.com.chart.enterative.entity.FileUpload;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.ProductHighlight;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductHighlightVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.FILE_TYPE;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductHighlightConverterService extends ConverterService<ProductHighlight, ProductHighlightVO> {

    @Autowired
    private FileUploadDAO fileUploadDAO;

    public ProductHighlightConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ProductHighlight convert(ProductHighlightVO vo) {
        ProductHighlight entity = new ProductHighlight();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setProduct(this.reflectionUtils.asHollowLink(Product::new, vo.getProduct()));
        entity.setSequence(vo.getSequence());
        return entity;
    }

    @Override
    public ProductHighlightVO convert(ProductHighlight entity) {
        ProductHighlightVO vo = new ProductHighlightVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setProduct(this.reflectionUtils.asNamedLink(ProductVO::new, entity.getProduct()));
        if (Objects.nonNull(entity.getProduct())) {
            List<FileUpload> files = this.fileUploadDAO.findByObjectIDAndType(entity.getProduct().getId(), FILE_TYPE.PRODUCT_IMAGE);
            if (!files.isEmpty()) {
                FileUpload image = files.get(0);
                vo.getProduct().setImagem("data:image/png;base64, " + image.getFileData());
            }

            vo.getProduct().setDisplayName(entity.getProduct().getDisplayName());
        }
        vo.setSequence(entity.getSequence());
        return vo;
    }
}
