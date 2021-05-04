package br.com.chart.enterative.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.service.activation.WebActivationService;
import br.com.chart.enterative.vo.CartaoVO;
import br.com.chart.enterative.vo.ServiceResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ActivationController extends BaseWebController {

    @Autowired
    private WebActivationService ativacaoService;

    @Autowired
    private HelperConverterService helperConverterService;

    private static final String CARTAO_FISICO_HOME = "ativacao/fisico/cartao";
    private static final String CARTAO_FISICO_ERROR = "ativacao/fisico/error";
    private static final String CARTAO_FISICO_ATIVAR = "ativacao/fisico/ativarCartao";
    private static final String CARTAO_FISICO_PEDIDO = "ativacao/fisico/orderdone";
    private static final String CARTAO_VIRTUAL_PEDIDO = "ativacao/virtual/orderdone";
    private static final String CARTAO_FISICO_ESPERA_RESPOSTA = "ativacao/espera";
    private static final String CARTAO_FISICO_ESPERA_SCANNER = "ativacao/scanner";
    private static final String CARTAO_FISICO_ESPERA_SCANNER_FAST = "ativacao/scanner/fast";

    @RequestMapping(value = "ativacao/status/{id}", method = RequestMethod.GET)
    public String ativacao_status__id(@PathVariable("id") Long id) {
        this.ativacaoService.verifyActivationStatus(id, loggedUser());
        return String.format("redirect:/user/order/edt/%s", id.toString());
    }

    @RequestMapping(value = "ativacao/fisico/orderdone/{id}", method = RequestMethod.GET)
    public ModelAndView ativacao_fisico_orderdone__id(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView(CARTAO_FISICO_PEDIDO);
        this.ativacaoService.fillView_OrderDone(mv, id);
        return mv;
    }

    @RequestMapping(value = "ativacao/virtual/orderdone/{id}", method = RequestMethod.GET)
    public ModelAndView ativacao_virtual_orderdone__id(@PathVariable("id") Long id) {
        ModelAndView mv = this.createView(CARTAO_VIRTUAL_PEDIDO);
        this.ativacaoService.fillView_OrderDoneVirtual(mv, id);
        return mv;
    }

    @RequestMapping(value = "ativacao/fisico/customer/payment/done/cielo", method = RequestMethod.GET)
    public ModelAndView ativacao_fisico_customer_payment_done_cielo() {
        ServiceResponse result;
        CartaoVO cartao;

        try {
            result = this.ativacaoService.endPayment("CIELO", null, loggedUser());
            cartao = (CartaoVO) result.getResponse();
            return this.ativacao_fisico_balance_ativar(cartao);
        } catch (CRUDServiceException e) {
            result = e.getResponse();
            ModelAndView mv = this.createView(CARTAO_FISICO_ERROR);
            cartao = new CartaoVO();
            if (e.isTranslated()) {
                cartao.setErrorMessage(result.getMessage());
            } else {
                cartao.setErrorMessage(this.helperConverterService.getMessage(result.getMessage()));
            }
            mv.addObject("cartao", cartao);
            return mv;
        }
    }

    @RequestMapping(value = "ativacao/fisico/customer/payment/done/enterative", method = RequestMethod.GET)
    public ModelAndView ativacao_fisico_customer_payment_done_enterative() {
        ServiceResponse result;
        CartaoVO cartao;

        try {
            result = this.ativacaoService.endPayment("ENTERATIVE", null, loggedUser());
            cartao = (CartaoVO) result.getResponse();
            return this.ativacao_fisico_balance_ativar(cartao);
        } catch (CRUDServiceException e) {
            result = e.getResponse();
            ModelAndView mv = this.createView(CARTAO_FISICO_ERROR);
            cartao = new CartaoVO();
            if (e.isTranslated()) {
                cartao.setErrorMessage(result.getMessage());
            } else {
                cartao.setErrorMessage(this.helperConverterService.getMessage(result.getMessage()));
            }
            mv.addObject("cartao", cartao);
            return mv;
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "ativacao/fisico/customer/payment/done/pagseguro", method = RequestMethod.GET)
    public ModelAndView ativacao_fisico_customer_payment_done_pagseguro(@RequestParam("transaction_id") String transactionId) {
        ServiceResponse result;
        CartaoVO cartao;

        try {
            result = this.ativacaoService.endPayment("PAGSEGURO", transactionId, loggedUser());
            cartao = (CartaoVO) result.getResponse();
            return this.ativacao_fisico_balance_ativar(cartao);
        } catch (CRUDServiceException e) {
            result = e.getResponse();
            ModelAndView mv = this.createView(CARTAO_FISICO_ERROR);
            cartao = new CartaoVO();
            if (e.isTranslated()) {
                cartao.setErrorMessage(result.getMessage());
            } else {
                cartao.setErrorMessage(this.helperConverterService.getMessage(result.getMessage()));
            }
            mv.addObject("cartao", cartao);
            return mv;
        }
    }

    @RequestMapping(value = "ativacao/fisico/customer/pay/{orderId}/{type}", method = RequestMethod.POST)
    public ModelAndView ativacao_fisico_customer_pay(@PathVariable("type") String method, @PathVariable("orderId") Long orderId) {
        ServiceResponse result;
        try {
            result = this.ativacaoService.initPayment(method, orderId);
            if (result.getResponseCode() != RESPONSE_CODE.E00) {
                ModelAndView mv = this.createView("ativacao/payment/select");
                mv.addObject("paymentList", this.ativacaoService.retrievePaymentMethods(loggedUser()));
                mv.addObject("orderId", orderId);
                mv.addObject("errorMessage", result.getMessage());
                return mv;
            }
        } catch (CRUDServiceException e) {
            result = e.getResponse();

            ModelAndView mv = this.createView("ativacao/payment/select");
            mv.addObject("paymentList", this.ativacaoService.retrievePaymentMethods(loggedUser()));
            mv.addObject("orderId", orderId);
            if (e.isTranslated()) {
                mv.addObject("errorMessage", result.getMessage());
            } else {
                mv.addObject("errorMessage", this.helperConverterService.getMessage(result.getMessage()));
            }
            return mv;
        }

        String url;
        switch (method) {
            case "PAGSEGURO":
                url = String.format("%s%s", result.get("url"), result.get("token"));
                break;
            case "CIELO":
                url = result.get("url");
                break;
            case "ENTERATIVE":
                url = result.get("url");
                break;
            default:
                url = "https://enterativeapk.tk/enterative";
                break;
        }
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "ativacao/fisico/customer/payment", method = RequestMethod.POST)
    public ModelAndView ativacao_fisico_customer_payment(CartaoVO cartao) {
        ServiceResponse result;
        try {
            result = this.ativacaoService.preparePayment(cartao, loggedUser());
            if (result.getResponseCode() != RESPONSE_CODE.E00) {
                ModelAndView mv = this.createView(CARTAO_FISICO_ERROR);
                cartao.setRetryAction("ativacao/fisico/cartao");
                cartao.setErrorMessage(result.getMessage());
                mv.addObject("cartao", cartao);
                return mv;
            }
        } catch (CRUDServiceException e) {
            result = e.getResponse();

            ModelAndView mv = this.createView(CARTAO_FISICO_ERROR);
            cartao.setRetryAction("ativacao/fisico/cartao");
            if (e.isTranslated()) {
                cartao.setErrorMessage(result.getMessage());
            } else {
                cartao.setErrorMessage(this.helperConverterService.getMessage(result.getMessage()));
            }
            mv.addObject("cartao", cartao);
            return mv;
        }

        ModelAndView mv = this.createView("ativacao/payment/select");
        mv.addObject("paymentList", this.ativacaoService.retrievePaymentMethods(loggedUser()));
        mv.addObject("orderId", result.get("orderId"));
        return mv;
    }

    @RequestMapping(value = "ativacao/virtual/espera/{number}", method = RequestMethod.GET)
    public ModelAndView ativacao_virtual_espera(@PathVariable("number") Integer number) {
        ModelAndView mv = this.createView("ativacao/esperaVirtual");
        mv.addObject("pedido", number);
        mv.addObject("info_title", "activation.activating");
        mv.addObject("info_desc", "activation.awaitwhileactivates");
        return mv;
    }

    @RequestMapping(value = "ativacao/fisico/balance/ativar", method = RequestMethod.POST)
    public ModelAndView ativacao_fisico_balance_ativar(CartaoVO cartao) {
        ServiceResponse result = this.ativacaoService.activateByBalance(cartao, loggedUser());

        ModelAndView mv;
        if (Objects.isNull(result.getMessage())) {
            mv = this.createView(CARTAO_FISICO_ESPERA_RESPOSTA);
            mv.addObject("pedido", result.get("pedido"));
            mv.addObject("info_title", "activation.activating");
            mv.addObject("info_desc", "activation.awaitwhileactivates");
        } else {
            mv = this.createView(CARTAO_FISICO_ERROR);
            cartao.setRetryAction(CARTAO_FISICO_ATIVAR);
            cartao.setErrorMessage(result.getMessage());
            mv.addObject("cartao", cartao);
        }

        return mv;
    }

    @RequestMapping(value = "ativacao/fisico/bhn/ativar", method = RequestMethod.POST)
    public ModelAndView ativacao_fisico_bhn_ativar(CartaoVO cartao) {
        ServiceResponse result = this.ativacaoService.activateByBHN(cartao, loggedUser());

        ModelAndView mv;
        if (Objects.isNull(result.getMessage())) {
            mv = this.createView(CARTAO_FISICO_ESPERA_RESPOSTA);
            mv.addObject("pedido", result.get("pedido"));
            mv.addObject("info_title", "activation.activating");
            mv.addObject("info_desc", "activation.awaitwhileactivates");
        } else {
            mv = this.createView(CARTAO_FISICO_ERROR);
            cartao.setRetryAction(CARTAO_FISICO_ATIVAR);
            cartao.setErrorMessage(result.getMessage());
            mv.addObject("cartao", cartao);
        }

        return mv;
    }

    @RequestMapping(value = CARTAO_FISICO_ESPERA_SCANNER)
    public ModelAndView ativacao_scanner() {
        ModelAndView mv = this.createView(CARTAO_FISICO_ESPERA_RESPOSTA);

        SaleOrder order = new SaleOrder();
        order.setId(null);
        mv.addObject("pedido", order);

        mv.addObject("info_title", "activation.barcodereader");
        mv.addObject("info_desc", "activation.barcodereaderinit");
        return mv;
    }

    @RequestMapping(value = CARTAO_FISICO_ESPERA_SCANNER_FAST)
    public ModelAndView ativacao_scanner_fast() {
        return this.ativacao_scanner();
    }

    @RequestMapping(value = "ativacao/fisico/fast/{barcode}")
    public ModelAndView ativacao_fisico_fast__barcode(@PathVariable("barcode") String barcode) {
        CartaoVO cartao = new CartaoVO();
        cartao.setBarcode(barcode);

        return this.ativacao_fisico_bhn_ativar(cartao);
    }

    @RequestMapping("ativacao/fisico/cartao")
    public ModelAndView ativacao_fisico_cartao(CartaoVO cartao) {
        ServiceResponse result = this.ativacaoService.createActivationView_FirstStep(cartao, loggedUser());

        ModelAndView mv;
        if (Objects.isNull(result.getMessage())) {
            mv = result.get("mv");
        } else {
            mv = this.createView(CARTAO_FISICO_ERROR);
            cartao.setRetryAction(CARTAO_FISICO_HOME);
            cartao.setErrorMessage(result.getMessage());
        }
        return mv;
    }

    @RequestMapping(value = "ativacao/fisico/ativarCartao", method = RequestMethod.POST)
    public ModelAndView ativacao_fisico_ativarcartao(CartaoVO cartao) {
        ServiceResponse result = this.ativacaoService.createActivationView_SecondStep(cartao, loggedUser());

        ModelAndView mv;
        if (Objects.isNull(result.getMessage())) {
            mv = (ModelAndView) result.get("mv");
        } else {
            mv = this.createView(CARTAO_FISICO_ERROR);
            cartao.setRetryAction(CARTAO_FISICO_HOME);
            cartao.setErrorMessage(result.getMessage());
        }

        mv.addObject("cartao", cartao);
        return mv;
    }
}
