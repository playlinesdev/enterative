package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.vo.ShoppingCartVO;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.store.ShoppingCartService;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Controller
public class ShoppingCartController extends BaseWebController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private HelperConverterService helperConverterService;

    @Autowired
    private SaleOrderCRUDService saleOrderService;

    private final String ERROR_VIEW = "cart/error";

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(path = "cart", method = RequestMethod.GET)
    public ModelAndView cart_get() {
        ModelAndView mv;
        try {
            if (Objects.isNull(this.loggedUser().getEmail()) || this.loggedUser().getEmail().isEmpty()) {
                throw new CRUDServiceException(new ServiceResponse().setMessage("Usuário sem email"), true);
            }
            ServiceResponse response = this.shoppingCartService.retrieveCart(this.loggedUserId());
            ShoppingCartVO cart = response.get("cart");
            mv = this.createView("cart/index");
            mv.addObject("cart", cart);
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }

        return mv;
    }

    @RequestMapping(path = "cart", method = RequestMethod.POST)
    public ModelAndView cart_post(ShoppingCartVO cart) {
        ModelAndView mv;
        try {
            this.shoppingCartService.processSave(cart, this.loggedUser());
            mv = this.createRedirectView("cart");
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }
        return mv;
    }

    @RequestMapping(path = "cart/add/{id}", method = RequestMethod.GET)
    public ModelAndView cart_add_id_get(@PathVariable("id") Long id) {
        ModelAndView mv;
        try {
            if (this.shoppingCartService.hasAdditionalSteps(id)) {
               mv = this.createRedirectView("store/step/" + id); 
            } else {
                this.shoppingCartService.addProduct(id, this.loggedUser());
                mv = this.createRedirectView("cart");
            }
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }
        return mv;
    }

    @RequestMapping(path = "cart/conclude", method = RequestMethod.GET)
    public ModelAndView cart_conclude_get() {
        ModelAndView mv;
        try {
            log.info("passou aqui");
            mv = this.createView("cart/conclude");
            ServiceResponse response = this.shoppingCartService.retrieveCart(this.loggedUserId());
            ShoppingCartVO cart = response.get("cart");
            
            this.shoppingCartService.validateCart(cart);
            
            mv.addObject("cart", cart);
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(path = "cart/pay", method = RequestMethod.GET)
    public ModelAndView cart_pay_get() {
        ServiceResponse result;
        ModelAndView mv;

        try {
            result = this.shoppingCartService.preparePayment(this.loggedUser());

            mv = this.createView("cart/payment");
            mv.addObject("paymentList", this.shoppingCartService.retrievePaymentMethods(loggedUser()));
            mv.addObject("orderId", result.get("orderId"));
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }
        return mv;
    }

    @RequestMapping(value = "cart/pay/{orderId}/{type}", method = RequestMethod.POST)
    public ModelAndView cart_pay_orderid_type_post(@PathVariable("type") String method, @PathVariable("orderId") Long orderId) {
        ServiceResponse result;
        try {
            result = this.shoppingCartService.initPayment(method, orderId);
            if (result.getResponseCode() != RESPONSE_CODE.E00) {
                ModelAndView mv = this.createView("cart/payment");
                mv.addObject("paymentList", this.shoppingCartService.retrievePaymentMethods(loggedUser()));
                mv.addObject("orderId", orderId);
                mv.addObject("errorMessage", result.getMessage());
                return mv;
            }
        } catch (CRUDServiceException e) {
            result = e.getResponse();

            ModelAndView mv = this.createView("cart/payment");
            mv.addObject("paymentList", this.shoppingCartService.retrievePaymentMethods(loggedUser()));
            mv.addObject("orderId", orderId);
            if (e.isTranslated()) {
                mv.addObject("errorMessage", result.getMessage());
            } else {
                mv.addObject("errorMessage", this.helperConverterService.getMessage(result.getMessage()));
            }
            return mv;
        }

//        ModelAndView mv = this.createView("ativacao/payment/awaitingpayment");
        String url;
        switch (method) {
            case "PAGSEGURO":
//                mv.addObject("paymentUrl", String.format("%s%s", result.get("url"), result.get("token")));
                url = String.format("%s%s", result.get("url"), result.get("token"));
                break;
            case "CIELO":
//                mv.addObject("paymentUrl", result.get("url"));
                url = result.get("url");
                break;

            case "ENTERATIVE":
                url = result.get("url");
                break;
            default:
                url = "https://enterativeapk.tk/enterative";
                break;
        }
//        return mv;
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "cart/pay/done/cielo", method = RequestMethod.GET)
    public ModelAndView cart_pay_done_cielo() {
        ServiceResponse result;

        try {
            result = this.shoppingCartService.endPayment("CIELO", null, loggedUser());
            SaleOrder order = (SaleOrder) result.getResponse();
            result = this.shoppingCartService.activateCards(order);
            if (result.getResponseCode() == RESPONSE_CODE.E00) {
                this.shoppingCartService.clear(loggedUser());
                return this.createRedirectView("cart/success");
            } else {
                ModelAndView mv = this.createView(this.ERROR_VIEW);
                mv.addObject("message", result.getMessage());
                return mv;
            }
        } catch (CRUDServiceException e) {
            e.printStackTrace();

            ModelAndView mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
            return mv;
        } catch (Exception e) {
            e.printStackTrace();

            ServiceResponse response = new ServiceResponse().handleException(e);
            ModelAndView mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = "cart/pay/done/enterative", method = RequestMethod.GET)
    public ModelAndView cart_pay_done_enterative() {
        ModelAndView mv;
        try {
            ServiceResponse result = this.shoppingCartService.activateCards(loggedUserId());
            if (result.getResponseCode() == RESPONSE_CODE.E00) {
                this.shoppingCartService.clear(loggedUser());
                mv = this.createRedirectView("cart/success");
            } else {
                mv = this.createView(this.ERROR_VIEW);
                mv.addObject("message", result.getMessage());
            }
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }

        return mv;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "cart/pay/done/pagseguro", method = RequestMethod.GET)
    public ModelAndView cart_pay_done_pagseguro(@RequestParam("transaction_id") String transactionId) {
        ServiceResponse result;
        ModelAndView mv;

        try {
            result = this.shoppingCartService.endPayment("PAGSEGURO", transactionId, loggedUser());
            SaleOrder order = (SaleOrder) result.getResponse();
            if (result.getResponseCode() == RESPONSE_CODE.E00) {
                result = this.shoppingCartService.activateCards(order);

                if (result.getResponseCode() == RESPONSE_CODE.E00) {
                    this.shoppingCartService.clear(loggedUser());
                    mv = this.createRedirectView("cart/success");
                } else {
                    mv = this.createView(this.ERROR_VIEW);
                    mv.addObject("message", this.helperConverterService.getMessage(result.getMessage()));
                }
            } else if (result.getResponseCode() == RESPONSE_CODE.E31) {
                this.shoppingCartService.clear(loggedUser());
                mv = this.createView(this.ERROR_VIEW);
                mv.addObject("message", this.helperConverterService.getMessage(result.getMessage()));
            } else {
                mv = this.createView(this.ERROR_VIEW);
                mv.addObject("message", this.helperConverterService.getMessage(result.getMessage()));
            }
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }

        return mv;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "cart/success", method = RequestMethod.GET)
    public ModelAndView cart_success() {
        ModelAndView mv;

        try {
            mv = this.createView("cart/success");
            mv.addObject("ordernumber", this.saleOrderService.findByCreatedByIdOrderByIdDesc(this.loggedUserId()).getId());
        } catch (CRUDServiceException e) {
            mv = this.createView(this.ERROR_VIEW);
            if (e.isTranslated()) {
                mv.addObject("message", e.getResponse().getMessage());
            } else {
                mv.addObject("message", this.helperConverterService.getMessage(e.getResponse().getMessage()));
            }
        } catch (Exception e) {
            ServiceResponse response = new ServiceResponse().handleException(e);
            mv = this.createView(this.ERROR_VIEW);
            mv.addObject("message", response.getMessage());
        }

        return mv;
    }
}
