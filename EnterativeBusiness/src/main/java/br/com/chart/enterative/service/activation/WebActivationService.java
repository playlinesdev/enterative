package br.com.chart.enterative.service.activation;

import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.USER_ROLE;
import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.dao.CieloTransactionResponseDAO;
import br.com.chart.enterative.dao.PagseguroTransactionResponseDAO;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.BHNActivationCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.service.payment.PaymentService;
import br.com.chart.enterative.vo.CartaoVO;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLine;
import br.com.chart.enterative.vo.payment.PaymentMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author William Leite
 */
@Service
public class WebActivationService extends BaseWebController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FluentService fluentService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;

    @Autowired
    private SaleOrderLineDAO saleOrderLineDAO;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private HelperConverterService helperConverterService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;

    @Autowired
    private CieloTransactionResponseDAO cieloResponseDao;

    @Autowired
    private PagseguroTransactionResponseDAO pagseguroResponseDao;

    @Autowired
    private ProductDAO productDAO;

    public ServiceResponse activate(SaleOrder order, User user) {
        ServiceResponse result = new ServiceResponse();
        try {
            String token = this.userCRUDService.generateToken(user.getLogin());
            this.userCRUDService.setTokenForID(token, user.getId());

            WSRequest solicitacao = this.createRequest(this.cartaoFromOrder(order), ACTIVATION_TYPE.PHYSICAL, user);
            final String endpoint = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_PATH);
            WSRequest response = this.send(solicitacao, token, endpoint, user.getLogin());

            if (Objects.nonNull(response)) {
                // Resposta do WSEnterative
                RESPONSE_CODE responseCode = response.getResponse();
                if (responseCode == RESPONSE_CODE.E00) {
                    // Resposta da BHN
                    responseCode = response.getLines().get(0).getResponseAux();
                    if (Objects.isNull(responseCode) || responseCode == RESPONSE_CODE.B00) {
                        result.put("wsResponse", response);
                    } else {
                        result.setMessage(this.utils.formatResponse(responseCode));
                    }
                } else {
                    result.setMessage(this.utils.formatResponse(responseCode));
                }
            } else {
                result.setMessage("Não foi possível enviar o pedido de ativação!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(String.format("Exception: %s", e.getMessage()));
        } finally {
            this.userCRUDService.setTokenForID(null, user.getId());
        }
        return result;
    }

    private ServiceResponse activate(CartaoVO cartao, User user) {
        ServiceResponse result = new ServiceResponse();

        result.setMessage(this.fillOrderDetails(cartao, user));

        if (Objects.isNull(result.getMessage())) {
            if (Objects.isNull(cartao.getNumeroPedido()) || cartao.getNumeroPedido().isEmpty()) {
                try {
                    String token = this.userCRUDService.generateToken(user.getLogin());
                    this.userCRUDService.setTokenForID(token, user.getId());
                    WSRequest solicitacao = this.createRequest(cartao, ACTIVATION_TYPE.PHYSICAL, user);

                    final String endpoint = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_PATH);
                    WSRequest response = this.send(solicitacao, token, endpoint, user.getLogin());

                    if (Objects.nonNull(response)) {
                        // Resposta do WSEnterative
                        RESPONSE_CODE responseCode = response.getResponse();
                        if (responseCode == RESPONSE_CODE.E00) {
                            // Resposta da BHN
                            responseCode = response.getLines().get(0).getResponseAux();
                            if (Objects.isNull(responseCode) || responseCode == RESPONSE_CODE.B00) {
                                result.put("wsResponse", response);
                            } else {
                                result.setMessage(this.utils.formatResponse(responseCode));
                            }
                        } else {
                            result.setMessage(this.utils.formatResponse(responseCode));
                        }
                    } else {
                        result.setMessage("Não foi possível enviar o pedido de ativação!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setMessage(String.format("Exception: %s", e.getMessage()));
                } finally {
                    this.userCRUDService.setTokenForID(null, user.getId());
                }
            } else {
                result.setMessage("Cartão em processo de ativação! Favor acompanhar em [Meus Pedidos].");
            }
        }

        return result;
    }

    /**
     * Ativa o cartão pelo fluxo BHN
     *
     * @param cartao
     * @param user
     * @return
     */
    public ServiceResponse activateByBHN(CartaoVO cartao, User user) {
        ServiceResponse result = this.activate(cartao, user);

        if (Objects.isNull(result.getMessage())) {
            WSRequest response = (WSRequest) result.get("wsResponse");

            SaleOrder order = this.createOrder(response, user);
            order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());
            result.put("pedido", order);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<PaymentMethod> retrievePaymentMethods(User user) {
        ServiceResponse result = new ServiceResponse();

        try {
            result = this.paymentService.retrievePaymentMethods(user);
            List<Map<String, Object>> methods = (List<Map<String, Object>>) result.getResponse();

            boolean enableCielo = this.paymentService.cieloEnabled(user.getShop().getId());

            return methods.stream().filter(m -> {
                return enableCielo || !Objects.equals(String.valueOf(m.get("type")), "CIELO");
            }).map(m -> {
                PaymentMethod method = new PaymentMethod();
                method.setImage(String.format("%s.png", String.valueOf(m.get("type"))));
                method.setType(String.valueOf(m.get("type")));
                return method;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse endPayment(String paymentMethod, String transactionId, User user) throws CRUDServiceException {
        ServiceResponse result = new ServiceResponse();
        try {
            switch (paymentMethod) {
                case "ENTERATIVE": {
                    SaleOrder order = this.saleOrderCRUDService.findByCreatedByOrderByIdDesc(user);
                    if (Objects.nonNull(order)) {
                        order.setStatus(SALE_ORDER_STATUS.PAID);
                        order.setPaymentManagerToken(null);
                        order.getLines().stream().forEach(l -> {
                            this.accountTransactionCRUDService.processSaleOrderLine(l, SALE_ORDER_LINE_STATUS.PENDING, false);
                        });
                        order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());
                        result.setResponseCode(RESPONSE_CODE.E00).setResponse(this.cartaoFromOrder(order));
                    } else {
                        String msg = this.helperConverterService.getMessage(RESPONSE_CODE.E28.getDescription());
                        result.setResponseCode(RESPONSE_CODE.E28).setMessage(msg);
                        throw new CRUDServiceException(result);
                    }
                }
                break;
                case "CIELO": {
                    SaleOrder order = this.saleOrderCRUDService.findByCreatedByOrderByIdDesc(user);
                    String orderNumber;
                    if (Objects.nonNull(order)) {
                        orderNumber = order.getPaymentManagerToken();
                        result = this.paymentService.checkPaymentCielo(orderNumber, this.paymentService.retrieveToken(user));
                        String status = result.get("status");
                        String reference = result.get("reference");

                        CieloTransactionResponse cieloResponse = new ObjectMapper().convertValue(result.get("entity"), CieloTransactionResponse.class);
                        if (Objects.nonNull(cieloResponse)) {
                            cieloResponse.setCreatedAt(new Date());
                            this.cieloResponseDao.saveAndFlush(cieloResponse);
                        }

                        switch (status) {
                            case "PAGO":
                                order.setStatus(SALE_ORDER_STATUS.PAID);
                                break;
                            case "PENDENTE":
                            case "AUTORIZADO":
                                order.setStatus(SALE_ORDER_STATUS.AWAITING_PAYMENT);
                                break;
                            case "CANCELADO":
                            case "NEGADO":
                            case "EXPIRADO":
                            case "NAO_FINALIZADO":
                            case "CHARGEBACK":
                                order.setStatus(SALE_ORDER_STATUS.DENIED);
                                break;
                        }
                        order.setPaymentGatewayToken(reference);

                        order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());

                        switch (order.getStatus()) {
                            case PAID:
                                result.setResponseCode(RESPONSE_CODE.E00).setResponse(this.cartaoFromOrder(order));
                                break;
                            case AWAITING_PAYMENT:
                                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E31));
                            default:
                                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E26), false);
                        }
                    } else {
                        throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E25), false);
                    }
                }
                break;
                case "PAGSEGURO": {
                    result = this.paymentService.checkPaymentPagseguro(transactionId, this.paymentService.retrieveToken(user));
                    String status = result.get("status");
                    String reference = result.get("reference");

                    PagseguroTransactionResponse pagseguroResponse = result.get("entity");
                    pagseguroResponse.setCreatedAt(new Date());
                    this.pagseguroResponseDao.saveAndFlush(pagseguroResponse);

                    SaleOrder order = this.saleOrderCRUDService.findByPaymentManagerToken(reference);
                    if (Objects.nonNull(order)) {
                        order.setPaymentTransactionId(transactionId);
                        switch (status) {
                            case "PAID":
                                order.setStatus(SALE_ORDER_STATUS.PAID);
                                break;
                            case "AWAITING_PAYMENT":
                            case "UNDER_ANALYSIS":
                            case "AVAILABLE":
                            case "IN_DISPUTE":
                                order.setStatus(SALE_ORDER_STATUS.AWAITING_PAYMENT);
                                break;
                            case "RETURNED":
                            case "CANCELED":
                                order.setStatus(SALE_ORDER_STATUS.DENIED);
                                break;
                        }

                        order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());

                        switch (order.getStatus()) {
                            case PAID:
                                result.setResponseCode(RESPONSE_CODE.E00).setResponse(this.cartaoFromOrder(order));
                                break;
                            case AWAITING_PAYMENT:
                                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E31));
                            default:
                                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E26), false);
                        }
                    } else {
                        throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E25), false);
                    }
                }
                break;
            }
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
        return result;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse initPayment(String method, Long orderId) {
        ServiceResponse result = new ServiceResponse();
        SaleOrder order = this.saleOrderCRUDService.findOne(orderId);

        try {
            switch (method) {
                case "PAGSEGURO":
                    result = this.paymentService.initPaymentPhysicalCard(method, order);
                    order.setPaymentGatewayToken(result.get("token"));
                    this.saleOrderCRUDService.saveAndFlush(order, this.loggedUserId());
                    break;
                case "CIELO":
                    result = this.paymentService.initPaymentPhysicalCard(method, order);
                    break;
                case "ENTERATIVE":
                    BigDecimal accountBalance = this.accountTransactionCRUDService.retrieveAccountBalance(this.loggedUser().getAccount().getId());
                    BigDecimal orderAmount = order.getAmount();
                    result.put("url", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ENTERATIVE_CARD_REDIRECT_URL));

                    if (accountBalance.compareTo(orderAmount) >= 0) {
                        result.setResponseCode(RESPONSE_CODE.E00);
                    } else {
                        String msg = this.helperConverterService.getMessage(RESPONSE_CODE.E28.getDescription());
                        result.setResponseCode(RESPONSE_CODE.E28).setMessage(msg);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }

        return result;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse preparePayment(CartaoVO cartao, User user) throws CRUDServiceException {
        this.fillOrderDetails(cartao, user);
        if (Objects.isNull(cartao.getNumeroPedido()) || cartao.getNumeroPedido().isEmpty()) {
            ServiceResponse result = new ServiceResponse();
            try {
                result = this.paymentService.createOrder(cartao, user);
                if (result.getResponseCode() == RESPONSE_CODE.E00) {
                    String token = String.valueOf(result.getResponse());
                    SaleOrder order = this.createOrder(cartao, token, user);

                    order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());
                    result = new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).put("orderId", order.getId());
                }
                return result;
            } catch (HttpHostConnectException e) {
                result.setMessage(this.helperConverterService.getMessage("paymentmanager.offline"));
                e.printStackTrace();
                throw new CRUDServiceException(result);
            } catch (CRUDServiceException e) {
                throw e;
            } catch (Exception e) {
                String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
                result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
                e.printStackTrace();
                throw new CRUDServiceException(result);
            }
        } else {
            throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E24), false);
        }
    }

    /**
     * Ativa o cartão pelo fluxo Saldo
     *
     * @param cartao
     * @param user
     * @return
     */
    public ServiceResponse activateByBalance(CartaoVO cartao, User user) {
        ServiceResponse result = new ServiceResponse();
        String balanceError = this.verifyBalance(cartao, user.getAccount());

        if (Objects.nonNull(balanceError)) {
            result.setMessage(balanceError);
        } else {
            result = this.activate(cartao, user);

            if (Objects.isNull(result.getMessage())) {
                WSRequest response = result.get("wsResponse");
                SaleOrder order = this.createOrder(response, user);
                order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());

                order.getLines().forEach(l -> {
                    this.accountTransactionCRUDService.processSaleOrderLine(l, l.getStatus(), false);
                });

                result.put("pedido", order);
            }
        }

        return result;
    }

    /**
     * Preenche a view de OrderDone
     *
     * @param mv
     * @param orderID
     */
    public void fillView_OrderDone(ModelAndView mv, Long orderID) {
        SaleOrder saleOrder = this.saleOrderCRUDService.findOne(orderID);
        mv.addObject("pedido", this.saleOrderCRUDService.converter().convert(saleOrder));
    }

    @Autowired
    private BHNActivationCRUDService bhnActivationCRUDService;

    public void fillView_OrderDoneVirtual(ModelAndView mv, Long orderID) {
        SaleOrder saleOrder = this.saleOrderCRUDService.findOne(orderID);
        SaleOrderVO vo = this.saleOrderCRUDService.converter().convert(saleOrder);

        Merchant merchant = saleOrder.getShop().getMerchant();
        String terminal = saleOrder.getCreatedBy().getTerminal();
        String shopCode = saleOrder.getShop().getCode();

        vo.getLines().forEach(l -> {
            String pin;
            BHNActivation activation = this.bhnActivationCRUDService.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, l.getExternalCode());
            if (Objects.nonNull(activation)) {
                pin = activation.getVoucher().getPin();
                l.setPin(pin);
            }
        });

        mv.addObject("pedido", vo);
    }

    /**
     * Verifica e atualiza o pedido e suas linhas de acordo com resposta de status do web service.
     *
     * @param orderID
     * @param user
     */
    public void verifyActivationStatus(Long orderID, User user) {
        SaleOrder saleOrder = this.saleOrderCRUDService.findOne(orderID);

        if (Objects.nonNull(saleOrder) && Objects.nonNull(saleOrder.getLines()) && !saleOrder.getLines().isEmpty()) {
            try {
                String token = this.userCRUDService.generateToken(user.getLogin());
                this.userCRUDService.setTokenForID(token, user.getId());

                final String endpoint = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_STATUS_PATH);
                saleOrder.getLines().forEach((SaleOrderLine l) -> {
                    WSRequest request = this.createRequest(l, user);
                    WSRequest result = this.send(request, token, endpoint, user.getLogin());

                    if (Objects.nonNull(result)) {
                        // Resposta do WSEnterative
                        RESPONSE_CODE responseCode = result.getResponse();
                        RESPONSE_CODE responseCodeAux = result.getLines().get(0).getResponseAux();

                        l.setResponse(responseCode);
                        l.setResponseAux(responseCodeAux);

                        SALE_ORDER_LINE_STATUS lineStatus = this.saleOrderCRUDService.retrieveLineStatusWithResponse(l, responseCode, responseCodeAux, request.getLines().get(0).getActivationStatus());
                        l.setStatus(lineStatus);

                        ServiceResponse vo = this.accountTransactionCRUDService.processSaleOrderLine(l, lineStatus, false);
                        if (Objects.nonNull(vo.getMessage())) {
                            throw new IllegalArgumentException(vo.getMessage());
                        }
                    }

                    l.setReturnDate(new Date());
                });

                SALE_ORDER_STATUS orderStatus = this.saleOrderCRUDService.retrieveOrderStatus(saleOrder.getLines());
                saleOrder.setStatus(orderStatus);

                this.saleOrderCRUDService.saveAndFlush(saleOrder, user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.userCRUDService.setTokenForID("", user.getId());
            }
        }
    }

    /**
     * Cria uma Solicitação baseado em um CartaoVO e um ACTIVATION_TYPE
     *
     * @param cartao
     * @param type
     * @param user
     * @return
     */
    public WSRequest createRequest(CartaoVO cartao, ACTIVATION_TYPE type, User user) {
        WSRequest request = new WSRequest();
        request.setLines(new ArrayList<>());

        WSRequestLine line = new WSRequestLine();
        line.setBarcode(cartao.getBarcode());
        line.setExternalCode(this.utils.generateExternalCode());
        line.setActivationProcess(ACTIVATION_PROCESS.BHN);

        if (Objects.nonNull(user.getShop())) {
            if (Objects.nonNull(user.getShop().getMerchant())) {
                request.setMerchant(user.getShop().getMerchant().getMerchantIdentifier());
            } else {
                throw new IllegalArgumentException("Loja do usuário não possui distribuidor cadastrado!");
            }
        } else {
            throw new IllegalArgumentException("Usuário não possui Loja cadastrada!");
        }

        if (Objects.nonNull(user.getTerminal()) && !user.getTerminal().isEmpty()) {
            request.setTerminal(user.getTerminal());
        } else {
            throw new IllegalArgumentException("Usuário não possui terminal cadastrado!");
        }

        request.setShop(user.getShop().getCode());
        request.setActivationType(type);
        request.setCallbackURL(this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_CALLBACKURL));
        request.getLines().add(line);
        return request;
    }

    /**
     * Cria um solicitação baseado em um SaleOrderLine
     *
     * @param item
     * @return
     */
    private WSRequest createRequest(SaleOrderLine item, User user) {
        WSRequest request = new WSRequest();
        request.setLines(new ArrayList<>());

        WSRequestLine line = new WSRequestLine();
        line.setExternalCode(item.getExternalCode());
        line.setActivationProcess(ACTIVATION_PROCESS.BHN);

        if (Objects.nonNull(user.getShop())) {
            if (Objects.nonNull(user.getShop().getMerchant())) {
                request.setMerchant(user.getShop().getMerchant().getMerchantIdentifier());
            } else {
                throw new IllegalArgumentException("Loja do usuário não possui distribuidor cadastrado!");
            }
        } else {
            throw new IllegalArgumentException("Usuário não possui Loja cadastrada!");
        }

        if (Objects.nonNull(user.getTerminal()) && !user.getTerminal().isEmpty()) {
            request.setTerminal(user.getTerminal());
        } else {
            throw new IllegalArgumentException("Usuário não possui terminal cadastrado!");
        }

        request.setShop(user.getShop().getCode());
        request.getLines().add(line);
        return request;
    }

    public CartaoVO cartaoFromOrder(SaleOrder order) {
        CartaoVO cartao = new CartaoVO();
        SaleOrderLine line = order.getLines().get(0);
        cartao.setBarcode(line.getBarcode());
        this.splitBarcode(cartao);
        cartao.setProduto(line.getProduct().getName());
        cartao.setProdutoImagem(this.utils.retrieveImagem(line.getProduct().getId()));
        cartao.setValor(line.getProduct().getAmount());
        return cartao;
    }

    public SaleOrder createOrder(CartaoVO cartao, String token, User user) {
        this.splitBarcode(cartao);

        Product product = this.productDAO.findByEanStartingWith(cartao.getEan());
        SaleOrder saleOrder = this.saleOrderCRUDService.initEntity();
        saleOrder.setCreatedAt(new Date());
        saleOrder.setStatus(SALE_ORDER_STATUS.PENDING);
        saleOrder.setType(SALE_ORDER_TYPE.VIRTUAL);
        saleOrder.setCreatedBy(user);
        saleOrder.setAmount(product.getAmount());
        saleOrder.setShop(user.getShop());
        saleOrder.setAccount(user.getAccount());
        saleOrder.setPaymentManagerToken(token);
        saleOrder.setLocale(this.locale().toLanguageTag());

        SaleOrderLine line = new SaleOrderLine();
        line.setReturnDate(null);
        line.setBarcode(cartao.getBarcode());
        line.setExternalCode(this.utils.generateExternalCode());
        line.setSaleOrder(saleOrder);
        line.setProduct(product);
        line.setCreatedAt(new Date());
        line.setCreatedBy(user);

        line.setStatus(SALE_ORDER_LINE_STATUS.PENDING);
        line.setAmount(product.getAmount());

        line.setResponse(null);
        line.setResponseAux(null);
        line.setCallbackStatus(CALLBACK_STATUS.PENDING);

        saleOrder.setLines(new ArrayList<>());
        saleOrder.getLines().add(line);

        return saleOrder;
    }

    public SaleOrder createOrder(WSRequest request, User user) throws IllegalArgumentException {
        WSRequestLine requestLine = request.getLines().get(0);
        CartaoVO cartao = new CartaoVO(requestLine.getBarcode());
        this.splitBarcode(cartao);
        Product produto;

        produto = this.productDAO.findByEanStartingWith(cartao.getEan());

        SaleOrder saleOrder = this.saleOrderCRUDService.initEntity();
        saleOrder.setCreatedAt(new Date());
        saleOrder.setStatus(SALE_ORDER_STATUS.PENDING);
        saleOrder.setType(SALE_ORDER_TYPE.PERSONALLY);
        saleOrder.setCreatedBy(user);
        saleOrder.setAmount(produto.getAmount());
        saleOrder.setShop(user.getShop());
        saleOrder.setAccount(user.getAccount());
        saleOrder.setLocale(this.locale().toLanguageTag());

        SaleOrderLine line = new SaleOrderLine();
        line.setReturnDate(null);
        line.setBarcode(requestLine.getBarcode());
        line.setExternalCode(requestLine.getExternalCode());
        line.setSaleOrder(saleOrder);
        line.setProduct(produto);
        line.setCreatedAt(new Date());
        line.setCreatedBy(user);

        line.setStatus(SALE_ORDER_LINE_STATUS.PENDING);
        line.setAmount(produto.getAmount());

        line.setResponse(request.getResponse());
        line.setResponseAux(requestLine.getResponseAux());
        line.setCallbackStatus(CALLBACK_STATUS.PENDING);

        saleOrder.setLines(new ArrayList<>());
        saleOrder.getLines().add(line);

        return saleOrder;
    }

    /**
     * Preenche o cartão com os detalhes do pedido
     *
     * @param cartao
     * @param user
     * @return
     */
    public String fillOrderDetails(CartaoVO cartao, User user) {
        String result = null;
        try {
            SaleOrderLine line = this.saleOrderLineDAO.findByBarcodeAndStatus(cartao.getBarcode(), SALE_ORDER_LINE_STATUS.ACTIVATED);

            if (Objects.nonNull(line)) {
                if (!Objects.equals(line.getSaleOrder().getCreatedBy().getId(), user.getId())) {
                    result = "Pedido vinculado a este cartão não pertence ao seu usuário.";
                } else {
                    cartao.setNumeroPedido(line.getSaleOrder().getId().toString());
                    cartao.setStatusPedido(line.getSaleOrder().getStatus().name());
                    cartao.setResposta(this.utils.formatResponse(line.getResponse()));
                    cartao.setRespostaAux(this.utils.formatResponse(line.getResponseAux()));
                }
            } else {
                cartao.setNumeroPedido(null);
                cartao.setStatusPedido(null);
                cartao.setResposta(null);
                cartao.setRespostaAux(null);
            }
        } catch (IncorrectResultSizeDataAccessException ex) {
            result = "Mais de um pedido contendo este cartão foi encontrado!";
        }

        return result;
    }

    /**
     * Preenche o cartão com o código de barras separado em EAN e Número do Cartão
     *
     * @param cartao
     * @return
     */
    public String splitBarcode(CartaoVO cartao) {
        String barcode = cartao.getBarcode();
        String result = null;
        if (Objects.nonNull(barcode) && !barcode.isEmpty()) {
            try {
                cartao.setEan(barcode.substring(0, 12));
                cartao.setCardNo(barcode.substring(12));
            } catch (IndexOutOfBoundsException e) {
                result = "Código de barras inválido!";
            }
        } else {
            result = "Favor preencher o código de barras do cartão!";
        }
        return result;
    }

    /**
     * Preenche o cartão com os dados do Produto
     *
     * @param cartao
     * @param user
     * @return
     */
    public String processCard(CartaoVO cartao, User user) {
        String result = this.splitBarcode(cartao);
        if (Objects.isNull(result)) {
            Product product = this.productDAO.findByEanStartingWith(cartao.getEan());

            if (Objects.isNull(product) || product.getStatus() != STATUS.ACTIVE) {
                result = "Produto não encontrado! Favor conferir o código digitado e tente novamente.";
            } else {
                cartao.setProduto(product.getName());
                cartao.setProdutoImagem(this.utils.retrieveImagem(product.getId()));
                cartao.setValor(product.getAmount());

                result = this.fillOrderDetails(cartao, user);
            }
        }
        return result;
    }

    public ServiceResponse createActivationView_FirstStep(CartaoVO cartao, User user) {
        ServiceResponse result = new ServiceResponse();

        ModelAndView mv;
        if (this.userDAO.hasRole(user, USER_ROLE.ROLE_FAST_ACTIVATION)) {
            mv = this.createRedirectView("ativacao/scanner/fast");
        } else {
            mv = this.createView("ativacao/fisico/cartao");
            mv.addObject("cartao", Objects.isNull(cartao) ? new CartaoVO() : cartao);
        }

        result.put("mv", mv);
        return result;
    }

    public ServiceResponse createActivationView_SecondStep(CartaoVO cartao, User user) {
        ServiceResponse result = new ServiceResponse();

        result.setMessage(this.processCard(cartao, user));

        if (Objects.isNull(result.getMessage())) {

            ModelAndView mv;
            if (this.userDAO.hasRole(user, USER_ROLE.ROLE_BHN)) {
                mv = this.createView("ativacao/fisico/bhn");
            } else if (this.userDAO.hasRole(user, USER_ROLE.ROLE_ENTERATIVE_BALANCE)) {
                mv = this.createView("ativacao/fisico/balance");

                cartao.setShopBalance(this.accountTransactionCRUDService.retrieveAccountBalance(user.getAccount().getId()));
            } else if (this.userDAO.hasRole(user, USER_ROLE.ROLE_ENTERATIVE_CREDIT)) {
                mv = this.createView("ativacao/fisico/enterative_credit");
            } else if (this.userDAO.hasRole(user, USER_ROLE.ROLE_CUSTOMER)) {
                mv = this.createView("ativacao/fisico/customer");
            } else {
                throw new AccessDeniedException("Usuário não autorizado a executar esta ação!");
            }
            result.put("mv", mv);
        }

        return result;
    }

    /**
     * Verifica se a Loja do Usuário possui saldo
     *
     * @param cartao
     * @param account
     * @return
     */
    public String verifyBalance(CartaoVO cartao, Account account) {
        String result = null;
        if (Objects.nonNull(account)) {
            BigDecimal accountBalance = this.accountTransactionCRUDService.retrieveAccountBalance(account.getId());

            if (accountBalance.signum() < 1) {
                result = "Conta vinculada ao usuário não possui saldo!";
            } else {
                Product product = this.productDAO.findByEanStartingWith(cartao.getEan());
                BigDecimal finalBalance = accountBalance.subtract(product.getAmount());
                if (finalBalance.signum() < 0) {
                    result = "Conta vinculada ao usuário não possui saldo suficiente para este cartão!";
                }
            }
        } else {
            result = "Usuário não possui conta vinculada!";
        }

        return result;
    }

    /**
     * Envia uma solicitação ao webservice
     *
     * @param solicitacao
     * @param token
     * @param endpoint
     * @param login
     * @return
     */
    public WSRequest send(WSRequest solicitacao, String token, String endpoint, String login) {
        ObjectMapper objectMapper = new ObjectMapper();
        WSRequest retorno = null;
        try {

            String jsonInString = objectMapper.writeValueAsString(solicitacao);

            // Leitura de Variavel de ambiente para gerar artefatos para BHN
            // String exibirJson = System.getenv("WSENTERATIVE_EXIBIR_JSON");
            String exibirJson = "SIM";
            log.info("exibirJson:" + exibirJson);
            if ("SIM".equalsIgnoreCase(exibirJson)) {
                log.info("Envio: " + jsonInString);
            }

            String retornoSTR = this.fluentService.sendWSEnterative(endpoint, jsonInString, token, login);

            if ("SIM".equalsIgnoreCase(exibirJson)) {
                log.info("Retorno: " + retornoSTR);
            }

            if (Objects.nonNull(retornoSTR)) {
                retorno = objectMapper.readValue(retornoSTR, WSRequest.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.debug("Identificado um Time-out");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
