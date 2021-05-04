package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.vo.search.ProductSearchVO;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.SupplierVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.ProductCategoryCRUDService;
import br.com.chart.enterative.service.crud.ProductTextCRUDService;
import br.com.chart.enterative.service.crud.SupplierCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController extends BaseWebController {

    @Autowired
    private ProductCRUDService productService;

    @Autowired
    private SupplierCRUDService supplierService;
    
    @Autowired
    private ProductTextCRUDService productTextService;

    @Autowired
    private ProductCategoryCRUDService productCategoryService;

    @RequestMapping(path = "admin/product", method = RequestMethod.GET)
    public ModelAndView admin_product_get() {
        LastSearchVO<ProductSearchVO> lastSearch = this.retrieveLastSearch("admin/product", ProductSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_product_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }
        
        ModelAndView mv = this.createView("admin/product/list");
        mv.addObject("searchForm", new ProductSearchVO());
        mv.addObject("objectList", new ArrayList<ProductVO>());
        mv.addObject("addPath", "admin/product/add");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", PRODUCT_TYPE.ordered());
        mv.addObject("process_list", ACTIVATION_PROCESS.ordered());
        mv.addObject("category_list", this.productCategoryService.findAllVOSorted(Comparator.comparing(ProductCategoryVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/product", method = RequestMethod.POST)
    public ModelAndView admin_product_post(ProductSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/product/list");
        PageWrapper<ProductVO> products = this.productService.retrieve(searchForm, pageable, "admin/product");
        
        this.updateLastSearch("admin/product", searchForm, pageable);
        
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/product/add");
        mv.addObject("editPath", "admin/product/edit");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", PRODUCT_TYPE.ordered());
        mv.addObject("process_list", ACTIVATION_PROCESS.ordered());
        mv.addObject("category_list", this.productCategoryService.findAllVOSorted(Comparator.comparing(ProductCategoryVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/product/add", method = RequestMethod.GET)
    public ModelAndView admin_product_add(ProductVO product) {
        return this.createFormView(this.productService.initVO());
    }

    @RequestMapping(path = "admin/product/edit/{id}")
    public ModelAndView admin_product_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.productService.findOneVO(id));
    }

    @RequestMapping(value = "admin/product/save/propagateCommission", method = RequestMethod.POST)
    public ModelAndView admin_product_save_propagatecommission(ProductVO product) {
        ServiceResponse response;
        try {
            response = this.productService.propagateCommission(product);
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(product);
    }

    @RequestMapping(value = "admin/product/save", method = RequestMethod.POST)
    public ModelAndView admin_product_save(ProductVO product) {
        ServiceResponse response;
        try {
            response = this.productService.processSave(product, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, product);
    }

    private ModelAndView createFormView(ServiceResponse response, ProductVO product) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            product = (ProductVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(product);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ProductVO vo) {
        ModelAndView mv = this.createView("admin/product/form");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", PRODUCT_TYPE.ordered());
        mv.addObject("category_list", this.productCategoryService.findAllVOSorted(Comparator.comparing(ProductCategoryVO::getName)).collect(Collectors.toList()));
        mv.addObject("supplier_list", this.supplierService.findAllVOSorted(Comparator.comparing(SupplierVO::getName)).collect(Collectors.toList()));
        mv.addObject("terms_list", this.productTextService.findByType(PRODUCT_TEXT_TYPE.TERMS_AND_CONDITIONS));
        mv.addObject("instructions_list", this.productTextService.findByType(PRODUCT_TEXT_TYPE.ACTIVATION_INSTRUCTIONS));
        mv.addObject("process_list", ACTIVATION_PROCESS.ordered());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/product/save");
        mv.addObject("crudHomePath", "admin/product");
        return mv;
    }
}
