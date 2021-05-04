package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.service.crud.MerchantCategoryCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.MerchantCategorySearchVO;
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
public class MerchantCategoryController extends BaseWebController {

    @Autowired
    private MerchantCategoryCRUDService merchantCategoryCRUDService;

    @RequestMapping(path = "admin/merchantcategory", method = RequestMethod.GET)
    public ModelAndView admin_merchantcategory_get() {
        ModelAndView mv = this.createView("admin/merchantcategory/list");
        mv.addObject("searchForm", new MerchantCategorySearchVO());
        mv.addObject("objectList", new ArrayList<MerchantCategoryVO>());
        mv.addObject("addPath", "admin/merchantcategory/add");
        return mv;
    }

    @RequestMapping(path = "admin/merchantcategory", method = RequestMethod.POST)
    public ModelAndView admin_merchantcategory_post(MerchantCategorySearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/merchantcategory/list");
        PageWrapper<MerchantCategoryVO> merchants = this.merchantCategoryCRUDService.retrieve(searchForm, pageable, "admin/merchantcategory");
        mv.addObject("objectList", merchants.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", merchants);
        mv.addObject("addPath", "admin/merchantcategory/add");
        mv.addObject("editPath", "admin/merchantcategory/edit");
        return mv;
    }

    @RequestMapping(path = "admin/merchantcategory/add", method = RequestMethod.GET)
    public ModelAndView admin_merchantcategory_add(MerchantCategoryVO merchant) {
        return this.createFormView(this.merchantCategoryCRUDService.initVO());
    }

    @RequestMapping(path = "admin/merchantcategory/edit/{id}")
    public ModelAndView admin_merchantcategory_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.merchantCategoryCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/merchantcategory/save", method = RequestMethod.POST)
    public ModelAndView admin_merchantcategory_save(MerchantCategoryVO merchant) {
        ServiceResponse response;
        try {
            response = this.merchantCategoryCRUDService.processSave(merchant, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, merchant);
    }

    private ModelAndView createFormView(ServiceResponse response, MerchantCategoryVO merchant) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            merchant = (MerchantCategoryVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(merchant);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(MerchantCategoryVO vo) {
        ModelAndView mv = this.createView("admin/merchantcategory/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/merchantcategory/save");
        mv.addObject("crudHomePath", "admin/merchantcategory");
        return mv;
    }

}
