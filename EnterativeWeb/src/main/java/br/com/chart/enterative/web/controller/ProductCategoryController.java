package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.service.crud.ProductCategoryCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductCategorySearchVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Controller
public class ProductCategoryController extends BaseWebController {

    @Autowired
    private ProductCategoryCRUDService productCategoryCRUDService;

    @RequestMapping(path = "admin/productcategory", method = RequestMethod.GET)
    public ModelAndView admin_productcategory_get() {
        ModelAndView mv = this.createView("admin/productcategory/list");
        mv.addObject("searchForm", new ProductCategorySearchVO());
        mv.addObject("objectList", new ArrayList<ProductCategoryVO>());
        mv.addObject("addPath", "admin/productcategory/add");
        return mv;
    }

    @RequestMapping(path = "admin/productcategory", method = RequestMethod.POST)
    public ModelAndView admin_productcategory_post(ProductCategorySearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/productcategory/list");
        PageWrapper<ProductCategoryVO> categories = this.productCategoryCRUDService.retrieve(searchForm, pageable, "admin/productcategory");
        mv.addObject("objectList", categories.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", categories);
        mv.addObject("addPath", "admin/productcategory/add");
        mv.addObject("editPath", "admin/productcategory/edit");
        return mv;
    }

    @RequestMapping(path = "admin/productcategory/add", method = RequestMethod.GET)
    public ModelAndView admin_productcategory_add(ProductCategoryVO merchant) {
        return this.createFormView(this.productCategoryCRUDService.initVO());
    }

    @RequestMapping(path = "admin/productcategory/edit/{id}")
    public ModelAndView admin_productcategory_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.productCategoryCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/productcategory/save", method = RequestMethod.POST)
    public ModelAndView admin_productcategory_save(ProductCategoryVO merchant) {
        ServiceResponse response;
        try {
            response = this.productCategoryCRUDService.processSave(merchant, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, merchant);
    }

    private ModelAndView createFormView(ServiceResponse response, ProductCategoryVO merchant) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            merchant = (ProductCategoryVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(merchant);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ProductCategoryVO vo) {
        ModelAndView mv = this.createView("admin/productcategory/form");
        mv.addObject("activeObject", vo);
        mv.addObject("category_list", this.productCategoryCRUDService.findAllVOSorted(Comparator.comparing(ProductCategoryVO::getName)).collect(Collectors.toList()));
        mv.addObject("saveActionPath", "admin/productcategory/save");
        mv.addObject("crudHomePath", "admin/productcategory");
        return mv;
    }

}
