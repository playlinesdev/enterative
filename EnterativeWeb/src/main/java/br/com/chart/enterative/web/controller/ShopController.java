package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.enums.SHOP_PHONE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.service.crud.ShopProductCommissionTemplateCRUDService;
import br.com.chart.enterative.vo.search.ShopSearchVO;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.MerchantCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShopController extends BaseWebController {

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private MerchantCRUDService merchantService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private ShopProductCommissionTemplateCRUDService templateService;

    @RequestMapping(path = "admin/shop", method = RequestMethod.GET)
    public ModelAndView admin_shop_get() {
        LastSearchVO<ShopSearchVO> lastSearch = this.retrieveLastSearch("admin/shop", ShopSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_shop(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }

        ModelAndView mv = this.createView("admin/shop/list");
        mv.addObject("searchForm", new ShopSearchVO());
        mv.addObject("objectList", new ArrayList<ShopVO>());
        mv.addObject("addPath", "admin/shop/add");
        mv.addObject("status_list", STATUS.values());
        return mv;
    }

    @RequestMapping(path = "admin/shop", method = RequestMethod.POST)
    public ModelAndView admin_shop(ShopSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/shop/list");
        PageWrapper<ShopVO> shops = this.shopService.retrieveShops(searchForm, pageable, "admin/shop");

        this.updateLastSearch("admin/shop", searchForm, pageable);

        mv.addObject("objectList", shops.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", shops);
        mv.addObject("addPath", "admin/shop/add");
        mv.addObject("editPath", "admin/shop/edit");
        mv.addObject("status_list", STATUS.values());
        return mv;
    }

    @RequestMapping(path = "admin/shop/add", method = RequestMethod.GET)
    public ModelAndView admin_shop_add(ShopVO shop) {
        return this.createFormView(this.shopService.initVO());
    }

    @RequestMapping(path = "admin/shop/edit/{id}")
    public ModelAndView admin_shop_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.shopService.findOneVO(id));
    }

    @RequestMapping(value = "admin/shop/save/addphone", method = RequestMethod.POST)
    public ModelAndView admin_shop_save_addphone(ShopVO shop) {
        ServiceResponse response = this.shopService.addPhone(shop);
        return this.createFormView(response, shop);
    }

    @RequestMapping(value = "admin/shop/save/removephone/{id}", method = RequestMethod.POST)
    public ModelAndView admin_shop_save_removephone(ShopVO shop, @PathVariable("id") Long id) {
        ServiceResponse response = this.shopService.removePhone(shop, id);
        return this.createFormView(response, shop);
    }

    @RequestMapping(value = "admin/shop/save/applytemplate", method = RequestMethod.POST)
    public ModelAndView admin_shop_save_applytemplate(ShopVO shop) {
        ServiceResponse response = this.shopService.applyTemplate(shop);
        return this.createFormView(response, shop);
    }

    @RequestMapping(value = "admin/shop/save", method = RequestMethod.POST)
    public ModelAndView admin_shop_save(ShopVO shop) {
        ServiceResponse response;
        try {
            response = this.shopService.processSave(shop, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, shop);
    }

    private ModelAndView createFormView(ServiceResponse response, ShopVO shop) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            shop = (ShopVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(shop);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(ShopVO vo) {
        ModelAndView mv = this.createView("admin/shop/form");
        mv.addObject("merchant_list", this.merchantService.findByStatusOrderByNameVO(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("template_list", this.templateService.findAllVO().collect(Collectors.toList()));
        mv.addObject("phone_type_list", SHOP_PHONE_TYPE.ordered());
        mv.addObject("status_list", STATUS.values());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/shop/save");
        mv.addObject("crudHomePath", "admin/shop");
        return mv;
    }
}
