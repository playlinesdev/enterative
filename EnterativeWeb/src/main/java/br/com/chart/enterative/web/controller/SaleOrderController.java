package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderLineCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ProductSearchVO;
import br.com.chart.enterative.vo.search.SaleOrderSearchVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SaleOrderController extends BaseWebController {

    @Autowired
    private SaleOrderCRUDService saleService;
    
    @Autowired
    private SaleOrderLineCRUDService saleLineService;

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @RequestMapping("admin/user/saleorder/removeexternalcode/{id}")
    public ModelAndView admin_user_saleorder_removeexternalcode_id(@PathVariable("id") Long id) {
        SaleOrder order = this.saleService.removeExternalCode(id);
        if (Objects.nonNull(order)) {
            return this.user_saleorder_edit_id(order.getId());
        } else {
            return new ModelAndView("redirect:/");
        }
    }

    @RequestMapping("user/saleorder/customerMobile/{id}/{mobile}")
    @ResponseBody
    public String updateOrderMobile(@PathVariable("id") Long orderID, @PathVariable("mobile") String mobile) {
        String result;
        try {
            this.saleService.setCustomerMobileForId(mobile, orderID);
            result = "OK";
        } catch (Exception e) {
            result = "error";
            e.printStackTrace();
        }
        return result;
    }

    private ModelAndView updateBalance(ModelAndView mv) {
        BigDecimal accountBalance = BigDecimal.ZERO;
        User user = this.loggedUser();
        if (Objects.nonNull(user.getAccount())) {
            accountBalance = this.accountTransactionService.retrieveAccountBalance(user.getAccount().getId());
        }
        mv.addObject("accountBalance", accountBalance);
        return mv;
    }
    
    @RequestMapping(path = "admin/saleorder/activate/{id}", method = RequestMethod.GET)
    public ModelAndView admin_saleorder_activate_get(@PathVariable("id") Long id) {
        this.saleService.activate(id);
        return this.user_saleorder_edit_id(id);
    }
    
    @RequestMapping(path = "admin/saleorder/cancel/{id}", method=RequestMethod.GET)
    public ModelAndView admin_saleorder_cancel_get(@PathVariable("id") Long id) {
        this.saleService.cancel(id);
        return this.user_saleorder_edit_id(id);
    }
    
    @RequestMapping(path = "admin/saleorder/line/activate/{id}/{saleorder}", method = RequestMethod.GET)
    public ModelAndView admin_saleorder_line_activate_get(@PathVariable("id") Long id, @PathVariable("saleorder") Long saleorder) {
        this.saleLineService.activate(id);
        return this.user_saleorder_edit_id(saleorder);
    }

    @RequestMapping(path = "user/saleorder", method = RequestMethod.GET)
    public ModelAndView user_saleorder_get() {
        LastSearchVO<SaleOrderSearchVO> lastSearch = this.retrieveLastSearch("user/saleorder", SaleOrderSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.user_saleorder(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }

        ModelAndView mv = this.createView("user/saleorder/list");
        mv.addObject("searchForm", new SaleOrderSearchVO());
        mv.addObject("objectList", new ArrayList<SaleOrderVO>());
        mv.addObject("shop_list", this.shopService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("status_list", SALE_ORDER_STATUS.ordered());
        mv.addObject("type_list", SALE_ORDER_TYPE.ordered());
        return this.updateBalance(mv);
    }

    @RequestMapping(path = "user/saleorder", method = RequestMethod.POST)
    public ModelAndView user_saleorder(SaleOrderSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("user/saleorder/list");
        PageWrapper<SaleOrderVO> orders = this.saleService.retrieveOrders(loggedUser(), searchForm, pageable, "user/saleorder");

        this.updateLastSearch("user/saleorder", searchForm, pageable);

        mv.addObject("objectList", orders.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("shop_list", this.shopService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("page", orders);
        mv.addObject("editPath", "user/saleorder/edit");
        mv.addObject("status_list", SALE_ORDER_STATUS.ordered());
        mv.addObject("type_list", SALE_ORDER_TYPE.ordered());
        return this.updateBalance(mv);
    }

    private ModelAndView createFormView(ServiceResponse response, SaleOrderVO order) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            order = response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(order);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(SaleOrderVO vo) {
        ModelAndView mv = this.createView("user/saleorder/form");
        mv.addObject("activeObject", vo);
        mv.addObject("crudHomePath", "user/saleorder");
        return this.updateBalance(mv);
    }

    @RequestMapping(path = "user/saleorder/edit/{id}")
    public ModelAndView user_saleorder_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.saleService.findOneVO(id));
    }
}