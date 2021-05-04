package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductTextVO;
import br.com.chart.enterative.enums.PRODUCT_TEXT_TYPE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.ProductTextCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductTextSearchVO;
import java.util.ArrayList;
import java.util.Objects;
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
public class ProductTextController extends BaseWebController {

    @Autowired
    private ProductTextCRUDService productTextCRUDService;

    @RequestMapping(path = "admin/producttext", method = RequestMethod.GET)
    public ModelAndView admin_producttext_get() {
        ModelAndView mv = this.createView("admin/producttext/list");
        mv.addObject("searchForm", new ProductTextSearchVO());
        mv.addObject("objectList", new ArrayList<ProductTextVO>());
        mv.addObject("addPath", "admin/producttext/add");
        return mv;
    }

    @RequestMapping(path = "admin/producttext", method = RequestMethod.POST)
    public ModelAndView admin_producttext_post(ProductTextSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/producttext/list");
        PageWrapper<ProductTextVO> products = this.productTextCRUDService.retrieve(searchForm, pageable, "admin/producttext");
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/producttext/add");
        mv.addObject("editPath", "admin/producttext/edit");
        return mv;
    }

    @RequestMapping(path = "admin/producttext/add", method = RequestMethod.GET)
    public ModelAndView admin_producttext_add(ProductTextVO product) {
        return this.createFormView(this.productTextCRUDService.initVO());
    }

    @RequestMapping(path = "admin/producttext/edit/{id}")
    public ModelAndView admin_producttext_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.productTextCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/producttext/save", method = RequestMethod.POST)
    public ModelAndView admin_producttext_save(ProductTextVO product) {
        ServiceResponse response;
        try {
            response = this.productTextCRUDService.processSave(product, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, product);
    }

    private ModelAndView createFormView(ServiceResponse response, ProductTextVO product) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            product = (ProductTextVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(product);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ProductTextVO vo) {
        ModelAndView mv = this.createView("admin/producttext/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/producttext/save");
        mv.addObject("crudHomePath", "admin/producttext");
        mv.addObject("type_list", PRODUCT_TEXT_TYPE.ordered());
        return mv;
    }

}
