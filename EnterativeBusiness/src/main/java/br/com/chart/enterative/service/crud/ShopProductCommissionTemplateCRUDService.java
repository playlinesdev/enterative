package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopDAO;
import br.com.chart.enterative.dao.ShopProductCommissionTemplateDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommissionTemplate;
import br.com.chart.enterative.entity.vo.*;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShopProductCommissionTemplateSearchVO;
import br.com.chart.enterative.vo.search.ShopSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionTemplateCRUDService extends UserAwareCRUDService<ShopProductCommissionTemplate, Long, ShopProductCommissionTemplateVO, ShopProductCommissionTemplateSearchVO> {

    @Autowired
    private ProductCRUDService productCRUDService;

    @Autowired
    private ShopProductCommissionTemplateLineCRUDService lineCRUDService;

    public ShopProductCommissionTemplateCRUDService(UserAwareDAO<ShopProductCommissionTemplate, Long> dao, ConverterService<ShopProductCommissionTemplate, ShopProductCommissionTemplateVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionTemplateDAO dao() {
        return (ShopProductCommissionTemplateDAO) super.dao();
    }

    @Override
    public ServiceResponse validate(ShopProductCommissionTemplateVO vo, CRUD_OPERATION operation) {
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

    @Override
    protected Supplier<ShopProductCommissionTemplate> initEntitySupplier() {
        return ShopProductCommissionTemplate::new;
    }

    @Override
    protected Supplier<ShopProductCommissionTemplateVO> initVOSupplier() {
        return () -> {
            ShopProductCommissionTemplateVO vo = new ShopProductCommissionTemplateVO();

            List<ProductVO> products = this.productCRUDService.findByStatusOrderByNameVO(STATUS.ACTIVE);
            vo.setLines(products.stream().map(this.lineCRUDService::initVO).collect(Collectors.toList()));
            return vo;
        };
    }

    @Override
    public ShopProductCommissionTemplateVO findOneVO(Long id) {
        ShopProductCommissionTemplateVO vo = super.findOneVO(id);
        List<ProductVO> products = this.productCRUDService.findByStatusOrderByNameVO(STATUS.ACTIVE);
        products.stream().forEach(p -> {
            if (vo.getLines().stream().noneMatch(l -> Objects.equals(l.getProduct().getId(), p.getId()))) {
                vo.getLines().add(this.lineCRUDService.initVO(p));
            }
        });
        return vo;
    }
}