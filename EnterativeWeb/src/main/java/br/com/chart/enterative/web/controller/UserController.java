package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.PartnerVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.PublicUserService;
import br.com.chart.enterative.service.base.CRUDService;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.PartnerCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.vo.ChangePasswordVO;
import br.com.chart.enterative.vo.ForgotPasswordVO;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.UserSearchVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends BaseWebController {

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private PublicUserService publicUserService;

    @Autowired
    private PartnerCRUDService partnerCRUDService;

    @Autowired
    private UserCRUDService userCRUDService;

    @RequestMapping(path = "admin/user", method = RequestMethod.GET)
    public ModelAndView admin_user_get() {
        ModelAndView mv = this.createView("admin/user/list");
        mv.addObject("searchForm", new UserSearchVO());
        mv.addObject("objectList", new ArrayList<UserVO>());
        mv.addObject("addPath", "admin/user/add");
        return mv;
    }

    @RequestMapping(path = "admin/user", method = RequestMethod.POST)
    public ModelAndView admin_user_post(UserSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/user/list");
        PageWrapper<UserVO> products = this.userCRUDService.retrieve(searchForm, pageable, "admin/user");
        mv.addObject("objectList", products.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", products);
        mv.addObject("addPath", "admin/user/add");
        mv.addObject("editPath", "admin/user/edit");
        return mv;
    }

    @RequestMapping(path = "admin/user/add", method = RequestMethod.GET)
    public ModelAndView admin_user_add() {
        return this.createFormView(this.userCRUDService.initVO());
    }

    @RequestMapping(path = "admin/user/edit/{id}")
    public ModelAndView admin_user_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.userCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/user/save", method = RequestMethod.POST)
    public ModelAndView admin_user_save(UserVO user) {
        ServiceResponse response;
        try {
            response = this.userCRUDService.processSave(user, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, user);
    }

    private ModelAndView createFormView(ServiceResponse response, UserVO product) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            product = (UserVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(product);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(UserVO vo) {
        ModelAndView mv = this.createView("admin/user/form");
        mv.addObject("status_list", STATUS.values());
        mv.addObject("account_list", this.accountService.findAllVOSorted(Comparator.comparing(AccountVO::getName)).collect(Collectors.toList()));
        mv.addObject("partner_list", this.partnerCRUDService.findAllVOSorted(Comparator.comparing(PartnerVO::getName)).collect(Collectors.toList()));
        mv.addObject("shop_list", this.shopService.findAllVOSorted(Comparator.comparing(ShopVO::getName)).collect(Collectors.toList()));
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/user/save");
        mv.addObject("crudHomePath", "admin/user");
        return mv;
    }

    @RequestMapping(path = "user/password", method = RequestMethod.GET)
    public ModelAndView user_password_get() {
        ModelAndView mv = this.createView("user/password");
        mv.addObject("userPassword", new ChangePasswordVO());
        return mv;
    }

    @RequestMapping(path = "user/password", method = RequestMethod.POST)
    public ModelAndView user_password_post(ChangePasswordVO vo) {
        ServiceResponse response = this.userCRUDService.changePassword(vo, loggedUser().getId());
        ModelAndView mv = this.createView("user/password");
        mv.addObject("userPassword", new ChangePasswordVO());
        mv.addObject("successful", Objects.isNull(response.getMessage()));
        mv.addObject("errorMessage", response.getMessage());
        return mv;
    }

    @RequestMapping(path = "user/new", method = RequestMethod.GET)
    public ModelAndView user_new_get() {
        ModelAndView mv = this.createView("user/new");
        mv.addObject("activeObject", this.userCRUDService.initVO());
        return mv;
    }

    @RequestMapping(path = "user/new/{partner}", method = RequestMethod.GET)
    public ModelAndView user_new_partner_get(@PathVariable("partner") Long partnerID) {
        ModelAndView mv = this.createView("user/new");
        PartnerVO partner = this.partnerCRUDService.findOneVO(partnerID);
        mv.addObject("activeObject", this.userCRUDService.initVO(partner));
        return mv;
    }

    @RequestMapping(path = "user/new", method = RequestMethod.POST)
    public ModelAndView user_new_post(UserVO vo) {
        ServiceResponse response;
        boolean translated = false;
        try {
            response = this.publicUserService.newCustomerUser(vo);
        } catch (CRUDServiceException e) {
            response = e.getResponse();
            translated = e.isTranslated();
        }

        if (Objects.nonNull(response.getMessage())) {
            vo.setEmail("");
            vo.setEmailMatch("");
            vo.setPassword("");
            vo.setPasswordMatch("");

            ModelAndView mv = this.createView("user/new");
            mv.addObject("activeObject", vo);
            mv.addObject("errorMessage", response.getMessage());
            mv.addObject("translated", translated);
            return mv;
        }

        return this.createView("user/new");
    }

    @RequestMapping(path = "user/email/{token}", method = RequestMethod.GET)
    public ModelAndView user_email_token_get(@PathVariable("token") String token) {
        ServiceResponse response;
        boolean translated = false;
        try {
            response = this.publicUserService.confirm(token);
        } catch (CRUDServiceException e) {
            response = e.getResponse();
            translated = e.isTranslated();
        }

        if (response.getResponseCode() != RESPONSE_CODE.E00) {
            ModelAndView mv = this.createView("user/email");
            mv.addObject("errorMessage", response.getMessage());
            mv.addObject("translated", translated);
            return mv;
        }

        return this.createView("user/email");
    }

    @RequestMapping(path = "user/forgotpassword", method = RequestMethod.GET)
    public ModelAndView user_forgotpassword() {
        ModelAndView mv = this.createView("user/forgotpassword");
        mv.addObject("forgotPassword", new ForgotPasswordVO());
        return mv;
    }

    @RequestMapping(path = "user/forgotpassword/{token}", method = RequestMethod.GET)
    public ModelAndView user_forgotpassword(@PathVariable("token") String token) {
        ServiceResponse response;
        boolean translated = false;
        try {
            response = this.userCRUDService.changePassword(token);
        } catch (CRUDServiceException e) {
            response = e.getResponse();
            translated = e.isTranslated();
        }

        Boolean successful = response.getResponseCode() == RESPONSE_CODE.E00;

        ModelAndView mv = this.createView("user/forgotpassword");
        mv.addObject("errorMessage", successful ? null : response.getMessage());
        mv.addObject("successful", successful);
        mv.addObject("translated", translated);
        return mv;
    }

    @RequestMapping(path = "user/forgotpassword", method = RequestMethod.POST)
    public ModelAndView user_forgotpassword_post(ForgotPasswordVO vo) {
        ServiceResponse response;
        boolean translated = false;
        try {
            response = this.publicUserService.createForgotPasswordToken(vo);
        } catch (CRUDServiceException e) {
            response = e.getResponse();
            translated = e.isTranslated();
        }

        Boolean successful = response.getResponseCode() == RESPONSE_CODE.E00;

        ModelAndView mv = this.createView("user/forgotpassword");
        mv.addObject("forgotPassword", new ForgotPasswordVO());
        mv.addObject("successful", successful);
        mv.addObject("errorMessage", successful ? null : response.getMessage());
        mv.addObject("translated", translated);
        return mv;
    }
}
