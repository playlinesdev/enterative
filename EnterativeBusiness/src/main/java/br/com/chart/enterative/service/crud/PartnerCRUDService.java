package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Partner;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PartnerSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PartnerCRUDService extends UserAwareCRUDService<Partner, Long, PartnerVO, PartnerSearchVO> {

    @Autowired
    private ProductDAO productDAO;

    public PartnerCRUDService(UserAwareDAO<Partner, Long> dao, ConverterService<Partner, PartnerVO> converter) {
        super(dao, converter);
    }

    public List<PartnerVO> findOrderByNameVO() {
        return this.findAllVOSorted(Comparator.comparing(PartnerVO::getName)).collect(Collectors.toList());
    }

    @Override
    protected Supplier<Partner> initEntitySupplier() {
        return Partner::new;
    }

    @Override
    protected Supplier<PartnerVO> initVOSupplier() {
        return () -> {
            PartnerVO vo = new PartnerVO();
            List<Product> products = this.productDAO.findByStatusOrderByName(STATUS.ACTIVE);
            vo.setProducts(new ArrayList<>());
            products.stream().forEach(p -> {
                ProductVO productVO = this.reflectionUtils.asNamedLink(ProductVO::new, p);
                productVO.setAvailable(false);
                vo.getProducts().add(productVO);
            });
            return vo;
        };
    }

    @Override
    public ServiceResponse validate(PartnerVO vo, CRUD_OPERATION operation) {
        ServiceResponse response = new ServiceResponse();
        switch (operation) {
            case CREATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case DELETE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case READ:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case UPDATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
        }
        return response;
    }
}
