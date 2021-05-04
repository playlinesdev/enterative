package br.com.chart.enterative.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.entity.vo.MerchantVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.MerchantCRUDService;
import br.com.chart.enterative.service.crud.MerchantCategoryCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.MerchantSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

@Controller
public class MerchantController extends BaseWebController {

    @Autowired
    private MerchantCRUDService merchantCRUDService;

    @Autowired
    private MerchantCategoryCRUDService merchantCategoryCRUDService;

    @RequestMapping(path = "admin/merchant", method = RequestMethod.GET)
    public ModelAndView admin_merchant_get() {
        ModelAndView mv = this.createView("admin/merchant/list");
        mv.addObject("searchForm", new MerchantSearchVO());
        mv.addObject("objectList", new ArrayList<MerchantVO>());
        mv.addObject("addPath", "admin/merchant/add");
        return mv;
    }

    @RequestMapping(path = "admin/merchant", method = RequestMethod.POST)
    public ModelAndView admin_merchant_post(MerchantSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/merchant/list");
        PageWrapper<MerchantVO> merchants = this.merchantCRUDService.retrieve(searchForm, pageable, "admin/merchant");
        mv.addObject("objectList", merchants.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", merchants);
        mv.addObject("addPath", "admin/merchant/add");
        mv.addObject("editPath", "admin/merchant/edit");
        return mv;
    }

    @RequestMapping(path = "admin/merchant/add", method = RequestMethod.GET)
    public ModelAndView admin_merchant_add(MerchantVO merchant) {
        return this.createFormView(this.merchantCRUDService.initVO());
    }

    @RequestMapping(path = "admin/merchant/edit/{id}")
    public ModelAndView admin_merchant_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.merchantCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/merchant/save", method = RequestMethod.POST)
    public ModelAndView admin_merchant_save(MerchantVO merchant) {
        ServiceResponse response;
        try {
            response = this.merchantCRUDService.processSave(merchant, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, merchant);
    }

    private ModelAndView createFormView(ServiceResponse response, MerchantVO merchant) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            merchant = (MerchantVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(merchant);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(MerchantVO vo) {
        ModelAndView mv = this.createView("admin/merchant/form");
        mv.addObject("status_list", STATUS.values());
        mv.addObject("category_list", this.merchantCategoryCRUDService.findAllVOSorted(Comparator.comparing(MerchantCategoryVO::getName)).collect(Collectors.toList()));
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/merchant/save");
        mv.addObject("crudHomePath", "admin/merchant");
        return mv;
    }
}
