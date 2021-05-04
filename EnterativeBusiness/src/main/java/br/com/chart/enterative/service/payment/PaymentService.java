package br.com.chart.enterative.service.payment;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.dao.CieloShopExceptionDAO;
import br.com.chart.enterative.dao.PagseguroPaymentMethodDAO;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.ShopDAO;
import br.com.chart.enterative.entity.CieloShopException;
import br.com.chart.enterative.entity.PagseguroPaymentMethod;
import br.com.chart.enterative.entity.PagseguroTransactionResponse;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShoppingCart;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.PAGSEGURO_PAYMENT_METHOD;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.vo.CartaoVO;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.payment.CieloResponse;
import br.com.chart.enterative.vo.payment.InitPaymentVO;
import br.com.chart.enterative.vo.payment.OrderRequestItemVO;
import br.com.chart.enterative.vo.payment.OrderRequestVO;
import br.com.chart.enterative.vo.payment.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.http.client.fluent.Response;
import org.joox.JOOX;
import org.joox.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

/**
 *
 * @author William Leite
 */
@Service
public class PaymentService extends BaseWebController {

    @Autowired
    private FluentService fluentService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private PagseguroPaymentMethodDAO pagseguroPaymentMethodDAO;

    @Autowired
    private ShopDAO shopDAO;
    
    @Autowired
    private CieloShopExceptionDAO cieloShopExceptionDAO;

    @Transactional
    public String retrieveToken(User user) {
        String partnerToken = user.getShop().getPaymentToken();

        if (Objects.isNull(partnerToken) || partnerToken.isEmpty()) {
            Long shopID = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_CUSTOMER_SHOP);
            Shop shop = this.shopDAO.findOne(shopID);
            partnerToken = shop.getPaymentToken();
        }
        return partnerToken;
    }

    private OrderRequestVO initRequest(ShoppingCart cart, User user) {
        OrderRequestVO vo = new OrderRequestVO();
        vo.setPartnerToken(this.retrieveToken(user));
        List<OrderRequestItemVO> lines = new ArrayList<>();

        AtomicInteger i = new AtomicInteger(0);
        cart.getLines().forEach(l -> {
            OrderRequestItemVO item = new OrderRequestItemVO();
            item.setProductId(l.getProduct().getId());
            item.setProductName(l.getProduct().getDisplayName());
            item.setQuantity(l.getQuantity());
            item.setSequence(i.getAndAdd(1));
            item.setUnitPrice(l.getAmount());
            lines.add(item);
        });

        vo.setItems(lines.toArray(new OrderRequestItemVO[0]));
        return vo;
    }

    private OrderRequestVO initRequest(CartaoVO cartao, User user) {
        OrderRequestVO vo = new OrderRequestVO();
        vo.setPartnerToken(this.retrieveToken(user));
        OrderRequestItemVO item = new OrderRequestItemVO();
        Product product = this.productDAO.findByName(cartao.getProduto());
        item.setProductId(product.getId());
        item.setProductName(cartao.getProduto());
        item.setQuantity(BigDecimal.ONE);
        item.setSequence(0);
        item.setUnitPrice(cartao.getValor());
        vo.setItems(new OrderRequestItemVO[]{item});
        return vo;
    }

    public ServiceResponse checkPaymentCielo(String orderNumber, String paymentToken) throws IOException {
        String url = String.format("%s%s/%s", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CHECK_PAYMENT_CIELO_URL), orderNumber, paymentToken);
        Response response = this.fluentService.sendSimpleJSON(url, "");
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);
        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).setResponse(paymentResponse.getResponse());
    }
    
    public PagseguroTransactionResponse pagseguroFromXml(String xml) throws SAXException, IOException {
        PagseguroTransactionResponse result = new PagseguroTransactionResponse();
        Match root = JOOX.$(new StringReader(xml));
        result.setReference(root.xpath("//reference").content());
        
        Match paymentMethod = root.xpath("//paymentMethod");
        result.setPaymentMethodCode(paymentMethod.xpath("//code").content());
        result.setPaymentMethodType(paymentMethod.xpath("//type").content());
        
        result.setType(root.xpath("//type").content());
        result.setStatus(root.xpath("//status").content());
        result.setCode(root.xpath("//code").content());
        result.setXml(xml);
        return result;
    }

    public ServiceResponse checkPaymentPagseguro(String transactionId, String paymentToken) throws IOException {
        String url = String.format("%s%s/%s", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CHECK_PAYMENT_PAGSEGURO_URL), transactionId, paymentToken);
        Response response = this.fluentService.sendSimpleJSON(url, "");
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);
        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).setResponse(paymentResponse.getResponse());
    }

    public ServiceResponse initPayment(String method, SaleOrder order, String redirectURL) throws IOException, SAXException {
        String url = this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_INIT_PAYMENT_URL);

        InitPaymentVO vo = new InitPaymentVO();
        vo.setOrderPaymentToken(order.getPaymentManagerToken());
        vo.setPaymentMethod(method);
        vo.setRedirectUrl(redirectURL);

        switch (method) {
            case "PAGSEGURO":
                List<PagseguroPaymentMethod> pagseguroPaymentMethods = this.pagseguroPaymentMethodDAO.findAll().filter(p -> p.getStatus() != STATUS.ACTIVE).collect(Collectors.toList());
                vo.setExcludedMethods(pagseguroPaymentMethods.stream().map(PagseguroPaymentMethod::getPaymentMethod).collect(Collectors.toList()).toArray(new PAGSEGURO_PAYMENT_METHOD[]{}));
                break;
        }

        Response response = this.fluentService.sendSimpleJSON(url, this.utils.toJSON(vo));
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);

        if (Objects.equals(paymentResponse.getResponseCode(), "000")) {
            ServiceResponse result = new ServiceResponse().setResponseCode(RESPONSE_CODE.E00);
            String responseString = paymentResponse.getResponse().toString();

            switch (method) {
                case "PAGSEGURO":
                    StringReader sr = new StringReader(responseString);
                    Match t = JOOX.$(sr);
                    result.put("token", t.xpath("//code").content());
                    result.put("url", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_PAGSEGURO_CHECKOUT));
                    break;
                case "CIELO":
                    CieloResponse cr = this.utils.fromJSON(responseString, CieloResponse.class);
                    result.put("url", cr.getSettings().getCheckoutUrl());
                    break;
            }
            return result;
        } else {
            return new ServiceResponse().setResponseCode(RESPONSE_CODE.E99).setMessage(paymentResponse.getMessage());
        }
    }

    public ServiceResponse initPaymentShoppingCart(String method, SaleOrder order) throws IOException, SAXException {
        String url = "";

        switch (method) {
            case "PAGSEGURO":
                url = this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_PAGSEGURO_CART_REDIRECT_URL);
                break;
            case "CIELO":
                url = this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CIELO_CART_REDIRECT_URL);
                break;
        }

        return this.initPayment(method, order, url);
    }

    public ServiceResponse initPaymentPhysicalCard(String method, SaleOrder order) throws IOException, SAXException {
        String url = "";

        switch (method) {
            case "PAGSEGURO":
                url = this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_PAGSEGURO_REDIRECT_URL);
                break;
            case "CIELO":
                url = this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CIELO_REDIRECT_URL);
                break;
        }

        return this.initPayment(method, order, url);
    }
    
    public boolean cieloEnabled(Long shopId) {
        List<CieloShopException> list = this.cieloShopExceptionDAO.findByShopId(shopId);
        return Objects.isNull(list) || list.isEmpty();
    }

    public ServiceResponse retrievePaymentMethods(User user) throws IOException, CRUDServiceException {
        String token = this.retrieveToken(user);
        String url = String.format("%s%s", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_METHOD_URL), token);
        Response response = this.fluentService.sendSimpleJSON(url, "");
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);
        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).setResponse(paymentResponse.getResponse());
    }

    public ServiceResponse createOrder(ShoppingCart cart, User user) throws JsonProcessingException, IOException {
        ServiceResponse result = new ServiceResponse();
        OrderRequestVO request = this.initRequest(cart, user);
        String json = this.utils.toJSON(request);
        Response response = this.fluentService.sendSimpleJSON(this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ORDER_URL), json);
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);

        if (Objects.equals(paymentResponse.getResponseCode(), "000")) {
            return result.setResponseCode(RESPONSE_CODE.E00).setResponse(String.valueOf(paymentResponse.getResponse()));
        } else {
            return result.setResponseCode(RESPONSE_CODE.E99).setMessage(paymentResponse.getMessage());
        }
    }

    public ServiceResponse createOrder(CartaoVO cartao, User user) throws JsonProcessingException, IOException {
        ServiceResponse result = new ServiceResponse();
        OrderRequestVO request = this.initRequest(cartao, user);
        String json = this.utils.toJSON(request);
        Response response = this.fluentService.sendSimpleJSON(this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ORDER_URL), json);
        PaymentResponse paymentResponse = this.utils.fromJSON(response.returnContent().toString(), PaymentResponse.class);

        if (Objects.equals(paymentResponse.getResponseCode(), "000")) {
            return result.setResponseCode(RESPONSE_CODE.E00).setResponse(String.valueOf(paymentResponse.getResponse()));
        } else {
            return result.setResponseCode(RESPONSE_CODE.E99).setMessage(paymentResponse.getMessage());
        }
    }
}
