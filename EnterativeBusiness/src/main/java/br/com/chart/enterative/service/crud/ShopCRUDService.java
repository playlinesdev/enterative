package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommissionTemplate;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopPhoneVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShopSearchVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopCRUDService extends UserAwareCRUDService<Shop, Long, ShopVO, ShopSearchVO> {

    @Autowired
    private ShopProductCommissionCRUDService shopProductCommissionCRUDService;

    @Autowired
    private ProductCRUDService productCRUDService;

    @Autowired
    private ShopProductCommissionTemplateCRUDService templateService;

    public ShopCRUDService(UserAwareDAO<Shop, Long> dao, ConverterService<Shop, ShopVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopDAO dao() {
        return (ShopDAO) super.dao();
    }

    public Shop findByAccountId(Long id) {
        return this.dao().findByAccountId(id);
    }

    public List<ShopVO> findByStatusOrderByNameVO(STATUS status) {
        return this.dao().findByStatusOrderByName(status).stream().map(this.converter()::convert).collect(Collectors.toList());
    }

    public List<Shop> findByStatusOrderByName(STATUS status) {
        return this.dao().findByStatusOrderByName(status);
    }
    
    public Shop findByCode(String code) {
        return this.dao().findByCode(code);
    }

    public PageWrapper<ShopVO> retrieveShops(ShopSearchVO searchForm, Pageable pageable, String url) {
        Page<Shop> page;
        if (Objects.nonNull(searchForm.getName())) {
            if (Objects.nonNull(searchForm.getCode())) {
                page = this.dao().findByNameIgnoreCaseContainingAndCodeIgnoreCaseContainingAndStatusOrderByName(searchForm.getName(), searchForm.getCode(), searchForm.getStatus(), pageable);
            } else {
                page = this.dao().findByNameIgnoreCaseContainingAndStatusOrderByName(searchForm.getName(), searchForm.getStatus(), pageable);
            }
        } else if (Objects.nonNull(searchForm.getCode())) {
            page = this.dao().findByCodeIgnoreCaseContainingAndStatusOrderByName(searchForm.getCode(), searchForm.getStatus(), pageable);
        } else {
            page = this.dao().findByStatusOrderByName(searchForm.getStatus(), pageable);
        }

        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    public ShopProductCommissionVO initProductCommissionVO() {
        return this.shopProductCommissionCRUDService.initVO();
    }

    public Shop findDefaultCustomerShop() {
        Long id = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_CUSTOMER_SHOP);
        return this.dao().findOne(id);
    }

    public ServiceResponse applyTemplate(ShopVO shopVO) {
        ServiceResponse result = new ServiceResponse();

        try {
            this.fill(shopVO);
            ShopProductCommissionTemplate template = this.templateService.findOne(shopVO.getTemplate().getId());
            template.getLines().stream().forEach(l -> {
                shopVO.getCommissions().stream().filter(c -> c.getProduct().getId() == l.getProduct().getId()).forEach(c -> {
                    c.setAmount(l.getAmount());
                    if (Objects.nonNull(l.getPercentage())) {
                        c.setPercentage(l.getPercentage().multiply(new BigDecimal(100)));
                    } else {
                        c.setPercentage(BigDecimal.ZERO);
                    }
                });
            });
        } catch (Exception e) {
            result.setMessage("Ocorreu um erro: %s", e.getMessage());
        } finally {
            result.put("entity", shopVO);
        }

        return result;
    }

    public ServiceResponse addPhone(ShopVO shopVO) {
        ServiceResponse result = new ServiceResponse();

        try {
            this.fill(shopVO);
            if (Objects.isNull(shopVO.getPhones())) {
                shopVO.setPhones(new ArrayList<>());
            }

            shopVO.getPhones().add(new ShopPhoneVO());
        } catch (Exception e) {
            result.setMessage("Ocorreu um erro: %s", e.getMessage());
        } finally {
            result.put("entity", shopVO);
        }

        return result;
    }

    public ServiceResponse removePhone(ShopVO shopVO, Long id) {
        ServiceResponse result = new ServiceResponse();

        try {
            this.fill(shopVO);
            if (Objects.isNull(shopVO.getPhones())) {
                shopVO.setPhones(new ArrayList<>());
            }

            shopVO.setPhones(
                    shopVO.getPhones().stream()
                            .filter(p -> {
                                if (Objects.equals(id, 0L)) {
                                    return Objects.nonNull(p.getId());
                                } else {
                                    return !Objects.equals(p.getId(), id);
                                }
                            })
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            result.setMessage("Ocorreu um erro: %s", e.getMessage());
        } finally {
            result.put("entity", shopVO);
        }

        return result;
    }

    @Override
    public ServiceResponse validate(ShopVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<Shop> initEntitySupplier() {
        return Shop::new;
    }

    @Override
    protected Supplier<ShopVO> initVOSupplier() {
        return () -> {
            ShopVO vo = new ShopVO();
            vo.setStatus(STATUS.ACTIVE);
            vo.setPhones(new ArrayList<>());

            List<ProductVO> products = this.productCRUDService.findByStatusOrderByNameVO(STATUS.ACTIVE);
            vo.setCommissions(products.stream().map(this.shopProductCommissionCRUDService::initVO).collect(Collectors.toList()));
            return vo;
        };
    }

    @Override
    public ServiceResponse processSave(Shop entity, Long user) throws CRUDServiceException {
        boolean hasCpf = Objects.nonNull(entity.getCpf()) && !entity.getCpf().isEmpty();
        boolean hasCnpj = Objects.nonNull(entity.getCnpj()) && !entity.getCnpj().isEmpty();
        
        if (!hasCpf && !hasCnpj) {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E17));
        }
        
        return super.processSave(entity, user);
    }

    @Override
    public ServiceResponse processSave(ShopVO vo, Long user) throws CRUDServiceException {
        boolean hasCpf = Objects.nonNull(vo.getCpf()) && !vo.getCpf().isEmpty();
        boolean hasCnpj = Objects.nonNull(vo.getCnpj()) && !vo.getCnpj().isEmpty();
        
        if (!hasCpf && !hasCnpj) {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E17));
        }
        
        return super.processSave(vo, user);
    }
    
    
}
