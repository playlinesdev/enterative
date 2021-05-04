package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.UserRoleVO;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.EnvParameterCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.service.crud.UserRoleCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.UserRoleSearchVO;
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
public class UserRoleController extends BaseWebController {

    @Autowired
    private UserRoleCRUDService userRoleService;

    @RequestMapping(path = "admin/userrole", method = RequestMethod.GET)
    public ModelAndView admin_userrole_get() {
        ModelAndView mv = this.createView("admin/userrole/list");
        mv.addObject("searchForm", new UserRoleSearchVO());
        mv.addObject("objectList", new ArrayList<UserRoleVO>());
        mv.addObject("addPath", "admin/userrole/add");
        return mv;
    }

    @RequestMapping(path = "admin/userrole", method = RequestMethod.POST)
    public ModelAndView admin_userrole_post(UserRoleSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/userrole/list");
        PageWrapper<UserRoleVO> products = this.userRoleService.retrieve(searchForm, pageable, "admin/userrole");
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/userrole/add");
        mv.addObject("editPath", "admin/userrole/edit");
        return mv;
    }

    @RequestMapping(path = "admin/userrole/add", method = RequestMethod.GET)
    public ModelAndView admin_userrole_add(UserRoleVO product) {
        return this.createFormView(this.userRoleService.initVO());
    }

    @RequestMapping(path = "admin/userrole/edit/{id}")
    public ModelAndView admin_userrole_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.userRoleService.findOneVO(id));
    }

    @RequestMapping(value = "admin/userrole/save", method = RequestMethod.POST)
    public ModelAndView admin_userrole_save(UserRoleVO userrole) {
        ServiceResponse response;
        try {
            response = this.userRoleService.processSave(userrole, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, userrole);
    }

    private ModelAndView createFormView(ServiceResponse response, UserRoleVO userrole) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            userrole = (UserRoleVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(userrole);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(UserRoleVO vo) {
        ModelAndView mv = this.createView("admin/userrole/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/userrole/save");
        mv.addObject("crudHomePath", "admin/userrole");
        return mv;
    }
}
