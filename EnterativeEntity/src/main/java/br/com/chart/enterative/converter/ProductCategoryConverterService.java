package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.entity.ProductCategory;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ProductCategoryConverterService extends ConverterService<ProductCategory, ProductCategoryVO> {

    @Autowired
    private HelperConverterService helperConverterService;

    public ProductCategoryConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public ProductCategory convert(ProductCategoryVO vo) {
        ProductCategory entity = new ProductCategory();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCode(vo.getCode());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setName(vo.getName());
        entity.setId(vo.getId());
        entity.setParent(this.reflectionUtils.asHollowLink(ProductCategory::new, vo.getParent()));
        return entity;
    }

    @Override
    public ProductCategoryVO convert(ProductCategory entity) {
        ProductCategoryVO vo = new ProductCategoryVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setChildren(entity.getChildren().stream().map(this::convert).collect(Collectors.toList()));
        vo.setCode(entity.getCode());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setDisplayName(this.retrieveDisplayName(entity));
        vo.setName(entity.getName());
        vo.setId(entity.getId());
        vo.setParent(this.reflectionUtils.asNamedLink(ProductCategoryVO::new, entity.getParent()));
        return vo;
    }

    private String retrieveDisplayName(ProductCategory entity) {
        List<String> names = new ArrayList<>();
        if (Objects.nonNull(entity.getParent())) {
            names.add(this.retrieveDisplayName(entity.getParent()));
        }
        names.add(this.helperConverterService.getMessage(entity.getName()));
        return names.stream().collect(Collectors.joining(" > "));
    }
}
