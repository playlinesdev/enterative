package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.Partner;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PartnerConverterService extends ConverterService<Partner, PartnerVO> {

    @Autowired
    private ProductDAO productDAO;

    public PartnerConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public Partner convert(PartnerVO vo) {
        Partner entity = new Partner();
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setName(vo.getName());
        entity.setProducts(vo.getProducts().stream()
                .filter(ProductVO::getAvailable)
                .map(p -> this.reflectionUtils.asHollowLink(Product::new, p))
                .collect(Collectors.toList()));
        return entity;
    }

    @Override
    public PartnerVO convert(Partner entity) {
        PartnerVO vo = new PartnerVO();
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setName(entity.getName());

        Stream<Product> productStream = this.productDAO.findByStatusOrderByName(STATUS.ACTIVE).stream();
        vo.setProducts(new ArrayList<>());

        if (Objects.nonNull(entity.getProducts()) && !entity.getProducts().isEmpty()) {
            productStream = productStream.filter(p -> entity.getProducts().stream().noneMatch(c -> Objects.equals(c.getId(), p.getId())));
            vo.getProducts().addAll(entity.getProducts().stream().map(p -> {
                ProductVO productVO = this.reflectionUtils.asNamedLink(ProductVO::new, p);
                productVO.setAvailable(true);
                return productVO;
            }).collect(Collectors.toList()));
        }

        productStream.forEach(p -> {
            ProductVO productVO = new ProductVO(p.getId(), p.getName());
            productVO.setAvailable(false);
            vo.getProducts().add(productVO);
        });

        return vo;
    }

}
