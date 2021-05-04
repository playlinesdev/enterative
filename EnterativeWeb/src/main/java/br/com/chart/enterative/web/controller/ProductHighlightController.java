package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ProductCategoryVO;
import br.com.chart.enterative.entity.vo.ProductHighlightVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.ProductCRUDService;
import br.com.chart.enterative.service.crud.ProductHighlightCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductHighlightSearchVO;
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
public class ProductHighlightController extends BaseWebController {

    @Autowired
    private ProductHighlightCRUDService productHighlightCRUDService;

    @Autowired
    private ProductCRUDService productCRUDService;

    @RequestMapping(path = "admin/producthighlight", method = RequestMethod.GET)
    public ModelAndView admin_producthighlight_get() {
        ModelAndView mv = this.createView("admin/producthighlight/list");
        mv.addObject("searchForm", new ProductHighlightSearchVO());
        mv.addObject("objectList", new ArrayList<ProductHighlightVO>());
        mv.addObject("addPath", "admin/producthighlight/add");
        return mv;
    }

    @RequestMapping(path = "admin/producthighlight", method = RequestMethod.POST)
    public ModelAndView admin_producthighlight_post(ProductHighlightSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/producthighlight/list");
        PageWrapper<ProductHighlightVO> highlights = this.productHighlightCRUDService.retrieve(searchForm, pageable, "admin/producthighlight");
        mv.addObject("objectList", highlights.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", highlights);
        mv.addObject("addPath", "admin/producthighlight/add");
        mv.addObject("editPath", "admin/producthighlight/edit");
        return mv;
    }

    @RequestMapping(path = "admin/producthighlight/add", method = RequestMethod.GET)
    public ModelAndView admin_producthighlight_add(ProductCategoryVO merchant) {
        return this.createFormView(this.productHighlightCRUDService.initVO());
    }

    @RequestMapping(path = "admin/producthighlight/edit/{id}")
    public ModelAndView admin_producthighlight_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.productHighlightCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/producthighlight/save", method = RequestMethod.POST)
    public ModelAndView admin_producthighlight_save(ProductHighlightVO highlight) {
        ServiceResponse response;
        try {
            response = this.productHighlightCRUDService.processSave(highlight, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, highlight);
    }

    private ModelAndView createFormView(ServiceResponse response, ProductHighlightVO highlight) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            highlight = (ProductHighlightVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(highlight);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ProductHighlightVO vo) {
        ModelAndView mv = this.createView("admin/producthighlight/form");
        mv.addObject("activeObject", vo);
        mv.addObject("product_list", this.productCRUDService.findAllVOSorted(Comparator.comparing(ProductVO::getName)).collect(Collectors.toList()));
        mv.addObject("saveActionPath", "admin/producthighlight/save");
        mv.addObject("crudHomePath", "admin/producthighlight");
        return mv;
    }

}
