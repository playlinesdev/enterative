package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.dao.UserDAO;
import br.com.chart.enterative.entity.User;

import java.math.BigDecimal;
import java.util.Objects;

import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MiscPageController extends BaseWebController {

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @RequestMapping("/")
    public ModelAndView principal() {
        ModelAndView mv = this.createView("index");
        User user = this.loggedUser();

        BigDecimal accountBalance = null;
        BigDecimal threshold = BigDecimal.ZERO;

        if (Objects.nonNull(user) && Objects.nonNull(user.getAccount())) {
            accountBalance = this.accountTransactionService.retrieveAccountBalance(user.getAccount().getId());
            if (Objects.nonNull(user.getAccount().getBalanceThreshold())) {
                threshold = user.getAccount().getBalanceThreshold();
            }
        }

        mv.addObject("accountBalance", accountBalance);
        mv.addObject("balanceThreshold", threshold);

        return mv;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return this.createView("login");
    }

    @RequestMapping(path = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView logout() {
        return this.createView("login");
    }

    @RequestMapping(path = "/loginsuccess")
    public String loginSuccess() {
        User user = this.loggedUser();
        if (Objects.nonNull(user.getLocale()) && !user.getLocale().isEmpty()) {
            return "redirect:/changelang/" + user.getLocale();
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(path = "/changelang/{lang}")
    public String changelang(@PathVariable("lang") String lang) {
        User user = this.loggedUser();
        user.setLocale(lang);
        userDAO.saveAndFlush(user, user.getId());
        return "redirect:/?lang=" + lang;
    }

    @RequestMapping("/sobre")
    public ModelAndView sobre() {
        return this.createView("sobre");
    }

    @RequestMapping(path = "/privacidade")
    public ModelAndView privacidade() {
        return this.createView("privacidade");
    }
}
