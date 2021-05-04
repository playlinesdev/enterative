package br.com.chart.enterative.service.store;

import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.ProductHighlightVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.ProductCategoryCRUDService;
import br.com.chart.enterative.service.crud.ProductHighlightCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.SortColumnVO;
import br.com.chart.enterative.vo.StoreStepVO;
import br.com.chart.enterative.vo.epay.EpayCatalog;
import br.com.chart.enterative.vo.search.StoreSearchVO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author William Leite
 */
@Service
public class StoreService extends UserAwareComponent {

    @Autowired
    private ProductHighlightCRUDService productHighlightCRUDService;

    @Autowired
    private ProductCRUDService productCRUDService;

    @Autowired
    private ProductCategoryCRUDService productCategoryCRUDService;

    @Autowired
    private EpayActivationProcess epayActivationProcess;

    @Autowired
    private EnterativeUtils utils;

    public List<ProductHighlightVO> retrieveHighlights(Long userID) {
        User user = this.userDAO.findOne(userID);
        List<ProductHighlightVO> result = this.productHighlightCRUDService.findAllVOSorted(Comparator.comparing(ProductHighlightVO::getSequence)).collect(Collectors.toList());
        if (Objects.nonNull(user.getPartner())) {
            List<Long> products = user.getPartner().getProducts().stream().map(Product::getId).collect(Collectors.toList());
            result = result.stream().filter(p -> products.contains(p.getProduct().getId())).collect(Collectors.toList());
        }
        if (Objects.nonNull(user.getShop())) {
            result = result.stream()
                    .filter(p -> user.getShop().getCommissions().stream()
                            .anyMatch((c) -> Objects.equals(c.getProduct().getId(), p.getProduct().getId()))
                    ).collect(Collectors.toList());
        }
        return result;
    }

    public String retrieveStepPath(Long productID) {
        Product product = this.productCRUDService.findOne(productID);
        if (Objects.nonNull(product)) {
            return product.getActivationProcess().name();
        }
        return null;
    }

    public List<ProductVO> retrieveFavorites(Long userID) {
        User user = this.userDAO.findByIdWithFavoriteProductsEager(userID);
        if (Objects.nonNull(user) && Objects.nonNull(user.getFavoriteProducts())) {
            List<ProductVO> result = user.getFavoriteProducts().stream().map(this.productCRUDService.converter()::convert).collect(Collectors.toList());
            if (Objects.nonNull(user.getPartner())) {
                List<Long> products = user.getPartner().getProducts().stream().map(Product::getId).collect(Collectors.toList());
                result = result.stream().filter(p -> products.contains(p.getId())).collect(Collectors.toList());
            }
            if (Objects.nonNull(user.getShop())) {
                result = result.stream().filter(p -> user.getShop().getCommissions().stream().anyMatch((c) -> Objects.equals(c.getProduct().getId(), p.getId()))).collect(Collectors.toList());
            }
            return result;
        }
        return null;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void toggleFavorite(Long productID, Long userID) throws CRUDServiceException {
        try {
            User user = this.userDAO.findByIdWithFavoriteProductsEager(userID);

            if (Objects.isNull(user.getFavoriteProducts())) {
                user.setFavoriteProducts(new ArrayList<>());
            }

            Boolean exists = user.getFavoriteProducts().stream().anyMatch(p -> Objects.equals(p.getId(), productID));
            if (exists) {
                user.getFavoriteProducts().removeIf(p -> Objects.equals(p.getId(), productID));
            } else {
                Product product = new Product();
                product.setId(productID);
                user.getFavoriteProducts().add(product);
            }

            this.userDAO.saveAndFlush(user, userID);
        } catch (Exception e) {
            throw new CRUDServiceException(new ServiceResponse());
        }
    }

    public Map<String, ?> retrieveStepContext(Long productID, StoreStepVO step) throws JsonProcessingException {
        Map<String, Object> context = new HashMap<>();

        Product product = this.productCRUDService.findOne(productID);
        switch (product.getActivationProcess()) {
            case EPAY:
                if (step.getCurrentStep() == 1L) {
                    context.put("displayCatalogs", this.epayActivationProcess.retrieveDisplayCatalogs());
                } else if (step.getCurrentStep() == 2L) {
                    if (Objects.equals(step.getDisplayCatalog().getProductType(), "TV")) {
                        context.put("catalogs", this.epayActivationProcess.retrieveCatalogs(step.getAreaCode(), step.getDisplayCatalog()));
                    }
                } else if (step.getCurrentStep() == 3L) {
                    if (Objects.equals(step.getDisplayCatalog().getProductType(), "Topup")) {
                        context.put("catalogs", this.epayActivationProcess.retrieveCatalogs(step.getAreaCode(), step.getDisplayCatalog()));
                    }
                } else if (step.getCurrentStep() == 4L) {
                    step.setPhone(step.getAreaCode());
                } else if (step.getCurrentStep() == 5L) {
                    EpayCatalog[] catalogs = this.epayActivationProcess.retrieveCatalogs(step.getAreaCode(), step.getDisplayCatalog());
                    step.setProduct(Arrays.stream(catalogs)
                            .filter(c -> Objects.equals(c.getProductId(), step.getProduct().getProductId()))
                            .findFirst().orElse(null));
                    step.setProductJson(this.utils.toJSON(step.getProduct()));
                }
                break;
            default:
                break;
        }

        return context;
    }

    public Long retrieveTotalSteps(Long productID) {
        Product product = this.productCRUDService.findOne(productID);
        switch (product.getActivationProcess()) {
            case EPAY:
                return 5L;
            default:
                return 0L;
        }
    }

    public ProductVO retrieveProduct(Long productID, Long userID) {
        ProductVO product = this.productCRUDService.findOneVO(productID);
        if (Objects.nonNull(product.getCategory())) {
            ProductCategoryVO category = this.productCategoryCRUDService.findOneVO(product.getCategory().getId());
            product.setCategory(category);
        }
        User user = this.userDAO.findByIdWithFavoriteProductsEager(userID);
        if (Objects.nonNull(user.getFavoriteProducts()) && !user.getFavoriteProducts().isEmpty()) {
            product.setFavorite(user.getFavoriteProducts().stream().anyMatch(p -> Objects.equals(p.getId(), productID)));
        } else {
            product.setFavorite(false);
        }
        return product;
    }

    public List<ProductVO> retrieveProducts(Long userID, PRODUCT_TYPE type) {
        User user = this.userDAO.findOne(userID);
        List<ProductVO> result = this.productCRUDService.findByStatusOrderByNameVO(STATUS.ACTIVE).stream()
                .filter(p -> (Objects.nonNull(p.getAmount()) && p.getAmount().signum() == 1) || Objects.isNull(p.getAmount()))
                .filter(p -> p.getType() == type)
                .collect(Collectors.toList());
        if (Objects.nonNull(user.getPartner())) {
            List<Long> products = user.getPartner().getProducts().stream().map(Product::getId).collect(Collectors.toList());
            result = result.stream().filter(p -> products.contains(p.getId())).collect(Collectors.toList());
        }
        if (Objects.nonNull(user.getShop())) {
            result = result.stream().filter(p -> user.getShop().getCommissions().stream().anyMatch((c) -> Objects.equals(c.getProduct().getId(), p.getId()))).collect(Collectors.toList());
        }
        return result;
    }

    public List<ProductVO> retrieveProductsWithoutImage(Long userID, PRODUCT_TYPE type) {
        User user = this.userDAO.findOne(userID);
        List<ProductVO> result = this.productCRUDService.findByStatusOrderByName(STATUS.ACTIVE).stream()
                .filter(p -> (Objects.nonNull(p.getAmount()) && p.getAmount().signum() == 1) || Objects.isNull(p.getAmount()))
                .filter(p -> p.getType() == type)
                .map(p -> this.productCRUDService.converter().convertWithoutImage(p))
                .collect(Collectors.toList());
        if (Objects.nonNull(user.getPartner())) {
            List<Long> products = user.getPartner().getProducts().stream().map(Product::getId).collect(Collectors.toList());
            result = result.stream().filter(p -> products.contains(p.getId())).collect(Collectors.toList());
        }
        if (Objects.nonNull(user.getShop())) {
            result = result.stream().filter(p -> user.getShop().getCommissions().stream().anyMatch((c) -> Objects.equals(c.getProduct().getId(), p.getId()))).collect(Collectors.toList());
        }
        return result;
    }

    public PageWrapper<ProductVO> search(StoreSearchVO searchForm, List<SortColumnVO> columns, Pageable pageable, String url, Long userID) {
        Page<Product> page;
        PageRequest request;

        if (Objects.nonNull(searchForm.getSortColumn())) {
            SortColumnVO column = columns.stream().filter(c -> Objects.equals(c.getId(), searchForm.getSortColumn().getId())).findFirst().get();
            request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), column.getDirection(), column.getColumnName());
        } else {
            request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        if (Objects.nonNull(searchForm.getName()) && !searchForm.getName().isEmpty()) {
            if (Objects.nonNull(searchForm.getProductCategory())) {
                page = this.productCRUDService.findByNameIgnoreCaseContainingAndCategoryIdAndStatus(searchForm.getName(), searchForm.getProductCategory().getId(), STATUS.ACTIVE, request);
            } else {
                page = this.productCRUDService.findByNameIgnoreCaseContainingAndStatus(searchForm.getName(), STATUS.ACTIVE, request);
            }
        } else {
            if (Objects.nonNull(searchForm.getProductCategory())) {
                page = this.productCRUDService.findByCategoryIdAndStatus(searchForm.getProductCategory().getId(), STATUS.ACTIVE, request);
            } else {
                page = this.productCRUDService.findByStatus(STATUS.ACTIVE, request);
            }
        }

        User user = this.userDAO.findOne(userID);
        List<Long> productIDs;
        if (Objects.nonNull(user.getPartner())) {
            productIDs = user.getPartner().getProducts().stream().map(Product::getId).collect(Collectors.toList());
        } else {
            productIDs = null;
        }

        return new PageWrapper<>(page.map(p -> {
            if (Objects.nonNull(user.getPartner()) && !productIDs.contains(p.getId())) {
                return null;
            }
            if (p.getActivationProcess() == ACTIVATION_PROCESS.BHN && p.getType() == PRODUCT_TYPE.CARD) {
                return null;
            }

            if (Objects.nonNull(user.getShop()) && !user.getShop().getCommissions().stream().anyMatch((c) -> Objects.equals(c.getProduct().getId(), p.getId()))) {
                return null;
            }

            ProductVO vo = this.productCRUDService.converter().convert(p);
            return vo;
        }), url);
    }

    public void incrementStep(StoreStepVO stepVO) {
        if (Objects.nonNull(stepVO.getDisplayCatalog())) {
            switch (stepVO.getDisplayCatalog().getProductType()) {
                case "Topup":
                    stepVO.setCurrentStep(stepVO.getCurrentStep() + 1L);
                    break;
                case "TV":
                    if (stepVO.getCurrentStep() == 3L) {
                        stepVO.setCurrentStep(5L);
                    } else {
                        stepVO.setCurrentStep(stepVO.getCurrentStep() + 1L);
                    }
                    break;
                case "PIN":
                    break;
            }
        } else {
            stepVO.setCurrentStep(stepVO.getCurrentStep() + 1L);
        }
    }
}
