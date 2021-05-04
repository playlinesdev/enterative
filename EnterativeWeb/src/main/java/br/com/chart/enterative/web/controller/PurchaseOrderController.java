package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.PurchaseOrderCRUDService;
import br.com.chart.enterative.service.crud.PurchaseOrderLineCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountSearchVO;
import br.com.chart.enterative.vo.search.PurchaseOrderSearchVO;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PurchaseOrderController extends BaseWebController {

    @Autowired
    private PurchaseOrderCRUDService purchaseOrderService;

    @Autowired
    private PurchaseOrderLineCRUDService purchaseOrderLineService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private ShopCRUDService shopService;

    private ModelAndView updateBalance(ModelAndView mv) {
        BigDecimal accountBalance = BigDecimal.ZERO;
        User user = this.loggedUser();
        if (Objects.nonNull(user.getAccount())) {
            accountBalance = this.accountTransactionService.retrieveAccountBalance(user.getAccount().getId());
        }
        mv.addObject("accountBalance", accountBalance);
        return mv;
    }

    @RequestMapping(path = "shop/purchaseorder", method = RequestMethod.GET)
    public ModelAndView shop_purchaseorder_get() {
        LastSearchVO<PurchaseOrderSearchVO> lastSearch = this.retrieveLastSearch("shop/purchaseorder", PurchaseOrderSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.shop_purchaseorder(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }

        ModelAndView mv = this.createView("shop/purchaseorder/list");
        mv.addObject("searchForm", new PurchaseOrderSearchVO());
        mv.addObject("objectList", new ArrayList<PurchaseOrderVO>());
        mv.addObject("shop_list", this.shopService.findByStatusOrderByNameVO(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("addPath", "shop/purchaseorder/add");
        return this.updateBalance(mv);
    }

    @RequestMapping(path = "shop/purchaseorder", method = RequestMethod.POST)
    public ModelAndView shop_purchaseorder(PurchaseOrderSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("shop/purchaseorder/list");
        PageWrapper<PurchaseOrderVO> orders = this.purchaseOrderService.retrieveOrders(loggedUser(), searchForm, pageable, "shop/purchaseorder");

        this.updateLastSearch("shop/purchaseorder", searchForm, pageable);

        mv.addObject("objectList", orders.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("shop_list", this.shopService.findByStatusOrderByNameVO(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("page", orders);
        mv.addObject("addPath", "shop/purchaseorder/add");
        mv.addObject("editPath", "shop/purchaseorder/edit");
        return this.updateBalance(mv);
    }

    @RequestMapping(path = "shop/purchaseorder/add", method = RequestMethod.GET)
    public ModelAndView shop_purchaseorder_add() {
        return this.createFormView(this.purchaseOrderService.initVO(loggedUserVO()));
    }

    @RequestMapping(value = "shop/purchaseorder/save/addline", method = RequestMethod.POST)
    public ModelAndView admin_shop_save_addline(PurchaseOrderVO order) {
        ServiceResponse response = this.purchaseOrderService.addLine(order);
        return this.createFormView(response, order);
    }

    @RequestMapping(value = "shop/purchaseorder/save/removeline/{id}", method = RequestMethod.POST)
    public ModelAndView admin_shop_save_removeline(PurchaseOrderVO order, @PathVariable("id") Long id) {
        ServiceResponse response = this.purchaseOrderService.removeLine(order, id);
        return this.createFormView(response, order);
    }

    private ModelAndView createFormView(ServiceResponse response, PurchaseOrderVO order) {
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

    private ModelAndView createFormView(PurchaseOrderVO vo) {
        ModelAndView mv = this.createView("shop/purchaseorder/form");
        mv.addObject("shop_list", this.shopService.findByStatusOrderByNameVO(STATUS.ACTIVE));
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("activeObject", this.purchaseOrderService.convertForUI(vo));
        mv.addObject("newLine", this.purchaseOrderLineService.initVO());
        mv.addObject("saveActionPath", "shop/purchaseorder/save");
        mv.addObject("crudHomePath", "shop/purchaseorder");
        return this.updateBalance(mv);
    }

    @RequestMapping(path = "shop/purchaseorder/edit/{id}")
    public ModelAndView shop_purchaseorder_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.purchaseOrderService.findOneVO(id));
    }

    @RequestMapping(path = "shop/purchaseorder/activate/{id}")
    public ModelAndView shop_purchaseorder_activate_id(@PathVariable("id") Long id) {
        ServiceResponse response = this.purchaseOrderService.activateOrder(id, loggedUser());
        return this.createFormView(response, this.purchaseOrderService.findOneVO(id));
    }

    @RequestMapping(path = "shop/purchaseorder/cancel/{id}", method=RequestMethod.GET)
    public ModelAndView shop_purchaseorder_cancel_get(@PathVariable("id") Long id) {
        this.purchaseOrderService.cancel(id);
        return this.shop_purchaseorder_edit_id(id);
    }

    @RequestMapping(path = "shop/purchaseorder/save", method = RequestMethod.POST)
    public ModelAndView shop_purchaseorder_save(PurchaseOrderVO purchaseOrder) {
        ServiceResponse response = this.purchaseOrderService.processSave(purchaseOrder, this.loggedUserId());
        return this.createFormView(response, purchaseOrder);
    }
}
