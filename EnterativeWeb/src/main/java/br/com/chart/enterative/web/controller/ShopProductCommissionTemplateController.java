package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.ShopProductCommissionTemplateVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.enums.SHOP_PHONE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.MerchantCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.service.crud.ShopProductCommissionTemplateCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShopProductCommissionTemplateSearchVO;
import br.com.chart.enterative.vo.search.ShopSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Objects;

@Controller
public class ShopProductCommissionTemplateController extends BaseWebController {

    @Autowired
    private ShopProductCommissionTemplateCRUDService templateService;

    @RequestMapping(path = "admin/shopcommissiontemplate", method = RequestMethod.GET)
    public ModelAndView admin_shopcommissiontemplate_get() {
        LastSearchVO<ShopProductCommissionTemplateSearchVO> lastSearch = this.retrieveLastSearch("admin/shopcommissiontemplate", ShopProductCommissionTemplateSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_shopcommissiontemplate(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }

        ModelAndView mv = this.createView("admin/shopcommissiontemplate/list");
        mv.addObject("searchForm", new ShopProductCommissionTemplateSearchVO());
        mv.addObject("objectList", new ArrayList<ShopProductCommissionTemplateVO>());
        mv.addObject("addPath", "admin/shopcommissiontemplate/add");
        mv.addObject("status_list", STATUS.values());
        return mv;
    }

    @RequestMapping(path = "admin/shopcommissiontemplate", method = RequestMethod.POST)
    public ModelAndView admin_shopcommissiontemplate(ShopProductCommissionTemplateSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/shopcommissiontemplate/list");
        PageWrapper<ShopProductCommissionTemplateVO> templates = this.templateService.retrieve(searchForm, pageable, "admin/shopcommissiontemplate");

        this.updateLastSearch("admin/shop", searchForm, pageable);

        mv.addObject("objectList", templates.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", templates);
        mv.addObject("addPath", "admin/shopcommissiontemplate/add");
        mv.addObject("editPath", "admin/shopcommissiontemplate/edit");
        return mv;
    }

    @RequestMapping(path = "admin/shopcommissiontemplate/add", method = RequestMethod.GET)
    public ModelAndView admin_shopcommissiontemplate_add(ShopVO shop) {
        return this.createFormView(this.templateService.initVO());
    }

    @RequestMapping(path = "admin/shopcommissiontemplate/edit/{id}")
    public ModelAndView admin_shopcommissiontemplate_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.templateService.findOneVO(id));
    }

    @RequestMapping(value = "admin/shopcommissiontemplate/save", method = RequestMethod.POST)
    public ModelAndView admin_shopcommissiontemplate_save(ShopProductCommissionTemplateVO template) {
        ServiceResponse response;
        try {
            response = this.templateService.processSave(template, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, template);
    }

    private ModelAndView createFormView(ServiceResponse response, ShopProductCommissionTemplateVO template) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            template = (ShopProductCommissionTemplateVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(template);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ShopProductCommissionTemplateVO vo) {
        ModelAndView mv = this.createView("admin/shopcommissiontemplate/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/shopcommissiontemplate/save");
        mv.addObject("crudHomePath", "admin/shopcommissiontemplate");
        return mv;
    }
}
