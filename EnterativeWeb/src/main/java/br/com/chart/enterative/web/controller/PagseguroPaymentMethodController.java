package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.vo.PagseguroPaymentMethodVO;
import br.com.chart.enterative.enums.PAGSEGURO_PAYMENT_METHOD;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.PagseguroPaymentMethodCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PagseguroPaymentMethodSearchVO;
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
public class PagseguroPaymentMethodController extends BaseWebController {

    @Autowired
    private PagseguroPaymentMethodCRUDService pagseguroPaymentMethodCRUDService;

    @RequestMapping(path = "admin/pagseguropaymentmethod", method = RequestMethod.GET)
    public ModelAndView admin_pagseguropaymentmethod_get() {
        ModelAndView mv = this.createView("admin/pagseguropaymentmethod/list");
        mv.addObject("searchForm", new PagseguroPaymentMethodSearchVO());
        mv.addObject("objectList", new ArrayList<PagseguroPaymentMethodVO>());
        mv.addObject("addPath", "admin/pagseguropaymentmethod/add");
        return mv;
    }

    @RequestMapping(path = "admin/pagseguropaymentmethod", method = RequestMethod.POST)
    public ModelAndView admin_pagseguropaymentmethod_post(PagseguroPaymentMethodSearchVO searchForm, Pageable pageable) {
        ModelAndView mv = this.createView("admin/pagseguropaymentmethod/list");
        PageWrapper<PagseguroPaymentMethodVO> merchants = this.pagseguroPaymentMethodCRUDService.retrieve(searchForm, pageable, "admin/pagseguropaymentmethod");
        mv.addObject("objectList", merchants.getContent());
        mv.addObject("searchForm", searchForm);
        mv.addObject("page", merchants);
        mv.addObject("addPath", "admin/pagseguropaymentmethod/add");
        mv.addObject("editPath", "admin/pagseguropaymentmethod/edit");
        return mv;
    }

    @RequestMapping(path = "admin/pagseguropaymentmethod/add", method = RequestMethod.GET)
    public ModelAndView admin_pagseguropaymentmethod_add(PagseguroPaymentMethodVO merchant) {
        return this.createFormView(this.pagseguroPaymentMethodCRUDService.initVO());
    }

    @RequestMapping(path = "admin/pagseguropaymentmethod/edit/{id}")
    public ModelAndView admin_pagseguropaymentmethod_edit_id(@PathVariable("id") Long id) {
        return this.createFormView(this.pagseguroPaymentMethodCRUDService.findOneVO(id));
    }

    @RequestMapping(value = "admin/pagseguropaymentmethod/save", method = RequestMethod.POST)
    public ModelAndView admin_pagseguropaymentmethod_save(PagseguroPaymentMethodVO merchant) {
        ServiceResponse response;
        try {
            response = this.pagseguroPaymentMethodCRUDService.processSave(merchant, this.loggedUserId());
        } catch (CRUDServiceException e) {
            response = e.getResponse();
        }
        return this.createFormView(response, merchant);
    }

    private ModelAndView createFormView(ServiceResponse response, PagseguroPaymentMethodVO merchant) {
        String errorMessage = null;
        if (Objects.isNull(response.getMessage())) {
            merchant = (PagseguroPaymentMethodVO) response.get("entity");
        } else {
            errorMessage = response.getMessage();
        }

        ModelAndView mv = this.createFormView(merchant);
        mv.addObject("errorMessage", errorMessage);
        return mv;
    }

    private ModelAndView createFormView(PagseguroPaymentMethodVO vo) {
        ModelAndView mv = this.createView("admin/pagseguropaymentmethod/form");
        mv.addObject("activeObject", vo);
        mv.addObject("saveActionPath", "admin/pagseguropaymentmethod/save");
        mv.addObject("crudHomePath", "admin/pagseguropaymentmethod");
        mv.addObject("status_list", STATUS.ordered());
        mv.addObject("method_list", PAGSEGURO_PAYMENT_METHOD.ordered());
        return mv;
    }

}
