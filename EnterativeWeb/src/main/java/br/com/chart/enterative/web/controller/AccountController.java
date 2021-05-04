package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTypeCRUDService;
import br.com.chart.enterative.vo.LastSearchVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountSearchVO;
import java.util.ArrayList;
import java.util.Comparator;
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

/**
 *
 * @author William Leite
 */
@Controller
public class AccountController extends BaseWebController {

    @Autowired
    private AccountCRUDService accountCRUDService;

    @Autowired
    private AccountTypeCRUDService accountTypeCRUDService;

    @RequestMapping(path = "admin/account", method = RequestMethod.GET)
    public ModelAndView admin_account_get() {
        LastSearchVO<AccountSearchVO> lastSearch = this.retrieveLastSearch("admin/account", AccountSearchVO.class);
        if (Objects.nonNull(lastSearch)) {
            return this.admin_account_post(lastSearch.getSearchForm(), PageRequest.of(lastSearch.getPageable().getPageNumber(), lastSearch.getPageable().getPageSize()));
        }
        
        ModelAndView mv = this.createView("admin/account/list");
        mv.addObject("searchForm", new AccountSearchVO());
        mv.addObject("objectList", new ArrayList<AccountVO>());
        mv.addObject("addPath", "admin/account/add");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", this.accountTypeCRUDService.findAllVOSorted(Comparator.comparing(AccountTypeVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/account", method = RequestMethod.POST)
    public ModelAndView admin_account_post(AccountSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/account/list");
        PageWrapper<AccountVO> accounts = this.accountCRUDService.retrieveAccounts(searchForm, pageable, "admin/account");
        
        this.updateLastSearch("admin/account", searchForm, pageable);
        
        mv.addObject("objectList", accounts.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", accounts);
        mv.addObject("addPath", "admin/account/add");
        mv.addObject("editPath", "admin/account/edit");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", this.accountTypeCRUDService.findAllVOSorted(Comparator.comparing(AccountTypeVO::getName)).collect(Collectors.toList()));
        return mv;
    }

    @RequestMapping(path = "admin/account/add", method = RequestMethod.GET)
    public ModelAndView admin_account_add(AccountVO account) {
        return this.createFormView(this.accountCRUDService.initVO());
    }

    @RequestMapping(path = "admin/account/edit/{id}")
    public ModelAndView admin_account_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.accountCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/account/save", method = RequestMethod.POST)
    public ModelAndView admin_account_save(AccountVO account) {
        ServiceResponse response;
        try {
            response = this.accountCRUDService.processSave(account, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, account);
    }

    private ModelAndView createFormView(ServiceResponse response, AccountVO account) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            account = (AccountVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(account);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(AccountVO vo) {
        ModelAndView mv = this.createView("admin/account/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/account/save");
        mv.addObject("crudHomePath", "admin/account");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("type_list", this.accountTypeCRUDService.findAllVOSorted(Comparator.comparing(AccountTypeVO::getName)).collect(Collectors.toList()));
        mv.addObject("parent_list", this.accountCRUDService.findByStatusOrderByName(STATUS.ACTIVE));
        return mv;
    }

}
