package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountTransactionCategoryCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTransactionCategorySearchVO;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountTransactionCategoryController extends BaseWebController {

    @Autowired
    private AccountTransactionCategoryCRUDService categoryService;

    @RequestMapping(path = "admin/accounttransactioncategory", method = RequestMethod.GET)
    public ModelAndView admin_accounttransactioncategory_get() {
        ModelAndView mv = this.createView("admin/accounttransactioncategory/list");
        mv.addObject("searchForm", new AccountTransactionCategorySearchVO());
        mv.addObject("objectList", new ArrayList<AccountTransactionCategoryVO>());
        mv.addObject("addPath", "admin/accounttransactioncategory/add");
        return mv;
    }

    @RequestMapping(path = "admin/accounttransactioncategory", method = RequestMethod.POST)
    public ModelAndView admin_accounttransactioncategory_post(AccountTransactionCategorySearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/accounttransactioncategory/list");
        PageWrapper<AccountTransactionCategoryVO> products = this.categoryService.retrieve(searchForm, pageable, "admin/accounttransactioncategory");
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/accounttransactioncategory/add");
        mv.addObject("editPath", "admin/accounttransactioncategory/edit");
        return mv;
    }

    @RequestMapping(path = "admin/accounttransactioncategory/add", method = RequestMethod.GET)
    public ModelAndView admin_accounttransactioncategory_add(AccountTransactionCategoryVO product) {
        return this.createFormView(this.categoryService.initVO());
    }

    @RequestMapping(path = "admin/accounttransactioncategory/edit/{id}")
    public ModelAndView admin_accounttransactioncategory_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.categoryService.findOneVO(id));
    }

    @RequestMapping(value = "admin/accounttransactioncategory/save", method = RequestMethod.POST)
    public ModelAndView admin_accounttransactioncategory_save(AccountTransactionCategoryVO category) {
        ServiceResponse response;
        try {
            response = this.categoryService.processSave(category, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, category);
    }

    private ModelAndView createFormView(ServiceResponse response, AccountTransactionCategoryVO product) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            product = (AccountTransactionCategoryVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(product);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(AccountTransactionCategoryVO vo) {
        ModelAndView mv = this.createView("admin/accounttransactioncategory/form");
        mv.addObject("status_list", STATUS.values());
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/accounttransactioncategory/save");
        mv.addObject("crudHomePath", "admin/accounttransactioncategory");
        return mv;
    }
}
