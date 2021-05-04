package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountTypeVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountTypeCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.AccountTypeSearchVO;
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
public class AccountTypeController extends BaseWebController {

    @Autowired
    private AccountTypeCRUDService accountTypeCRUDService;

    @RequestMapping(path = "admin/accounttype", method = RequestMethod.GET)
    public ModelAndView admin_accounttype_get() {
        ModelAndView mv = this.createView("admin/accounttype/list");
        mv.addObject("searchForm", new AccountTypeSearchVO());
        mv.addObject("objectList", new ArrayList<AccountTypeVO>());
        mv.addObject("addPath", "admin/accounttype/add");
        return mv;
    }

    @RequestMapping(path = "admin/accounttype", method = RequestMethod.POST)
    public ModelAndView admin_accounttype_post(AccountTypeSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/accounttype/list");
        PageWrapper<AccountTypeVO> types = this.accountTypeCRUDService.retrieve(searchForm, pageable, "admin/accounttype");
        mv.addObject("objectList", types.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", types);
        mv.addObject("addPath", "admin/accounttype/add");
        mv.addObject("editPath", "admin/accounttype/edit");
        return mv;
    }

    @RequestMapping(path = "admin/accounttype/add", method = RequestMethod.GET)
    public ModelAndView admin_accounttype_add(AccountTypeVO type) {
        return this.createFormView(this.accountTypeCRUDService.initVO());
    }

    @RequestMapping(path = "admin/accounttype/edit/{id}")
    public ModelAndView admin_accounttype_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.accountTypeCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/accounttype/save", method = RequestMethod.POST)
    public ModelAndView admin_accounttype_save(AccountTypeVO type) {
        ServiceResponse response;
        try {
            response = this.accountTypeCRUDService.processSave(type, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, type);
    }

    private ModelAndView createFormView(ServiceResponse response, AccountTypeVO type) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            type = (AccountTypeVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(type);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(AccountTypeVO vo) {
        ModelAndView mv = this.createView("admin/accounttype/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/accounttype/save");
        mv.addObject("crudHomePath", "admin/accounttype");
        return mv;
    }

}
