package br.com.chart.enterative.service.store;

import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.dao.CieloTransactionResponseDAO;
import br.com.chart.enterative.dao.PagseguroTransactionResponseDAO;
import br.com.chart.enterative.dao.ProductDAO;
import br.com.chart.enterative.entity.CieloTransactionResponse;
import br.com.chart.enterative.entity.PagseguroTransactionResponse;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.ShoppingCart;
import br.com.chart.enterative.entity.ShoppingCartLine;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.ShoppingCartLineVO;
import br.com.chart.enterative.entity.vo.ShoppingCartVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.SMS_STATUS;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.ShoppingCartCRUDService;
import br.com.chart.enterative.service.crud.ShoppingCartLineCRUDService;
import br.com.chart.enterative.service.crud.UserCRUDService;
import br.com.chart.enterative.service.payment.PaymentService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.StoreStepVO;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLine;
import br.com.chart.enterative.vo.WSRequestLineEpay;
import br.com.chart.enterative.vo.epay.EpayCatalog;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartService extends UserAwareComponent {

    @Autowired
    private ShoppingCartCRUDService cartService;

    @Autowired
    private ShoppingCartLineCRUDService lineService;

    @Autowired
    private EnterativeUtils utils;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private FluentService fluentService;

    @Autowired
    private HelperConverterService helperConverterService;

    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CieloTransactionResponseDAO cieloResponseDao;
    
    @Autowired
    private PagseguroTransactionResponseDAO pagseguroResponseDao;

    public ServiceResponse retrieveCart(Long userID) {
        ServiceResponse response = new ServiceResponse();
        try {
            ShoppingCart cart = this.cartService.findByCreatedById(userID);
            ShoppingCartVO vo = this.cartService.converter().convert(cart);
            return response.put("cart", vo).setResponseCode(RESPONSE_CODE.E00);
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            response.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(response, true);
        }
    }

    public void validateCart(ShoppingCartVO cart) throws CRUDServiceException {
        int maxItemsQuantityOfProducts = this.parameterDAO.get(ENVIRONMENT_PARAMETER.MAX_ITEM_PURCHASE_UNITS);
        for (ShoppingCartLineVO l : cart.getLines()) {
            if (Objects.equals(l.getProduct().getSendsSMS(), Boolean.TRUE) && StringUtils.isAllBlank(l.getUserCellphone())) {
                throw new CRUDServiceException(new ServiceResponse().setMessage(String.format("Produto [%s] necessita de número de celular para envio do PIN!", l.getProduct().getName())));
            }else if(l.getQuantity().floatValue() > maxItemsQuantityOfProducts){
                throw new CRUDServiceException(new ServiceResponse().setMessage(String.format("Não pode!")));
            }
        }
    }
    
    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse preparePayment(User user) throws CRUDServiceException {
        ShoppingCart cart = this.cartService.findByCreatedById(user.getId());
        ServiceResponse result;
        try {
            if (Objects.isNull(cart.getSaleOrder())) {
                result = this.paymentService.createOrder(cart, user);
                if (result.getResponseCode() == RESPONSE_CODE.E00) {
                    String token = String.valueOf(result.getResponse());
                    SaleOrder order = this.createOrder(cart, token, user);

                    order = this.saleOrderCRUDService.saveAndFlush(order, user.getId());
                    result = new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).put("orderId", order.getId());
                }
            } else {
                result = new ServiceResponse().setResponseCode(RESPONSE_CODE.E00).put("orderId", cart.getSaleOrder().getId());
            }
            return result;
        } catch (HttpHostConnectException e) {
            String message = this.helperConverterService.getMessage("paymentmanager.offline");
            result = new ServiceResponse().setMessage(message);
            e.printStackTrace();
            throw new CRUDServiceException(result);
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException(new ServiceResponse());
        }
    }

    @SuppressWarnings("unchecked")
    public List<PaymentMethod> retrievePaymentMethods(User user) throws CRUDServiceException {
        ServiceResponse result = new ServiceResponse();

        try {
            result = this.paymentService.retrievePaymentMethods(user);
            List<Map<String, Object>> methods = (List<Map<String, Object>>) result.getResponse();

            boolean enableCielo = this.paymentService.cieloEnabled(user.getShop().getId());

            return methods.stream().filter(m -> enableCielo || !Objects.equals(String.valueOf(m.get("type")), "CIELO")).map(m -> {
                PaymentMethod method = new PaymentMethod();
                method.setImage(String.format("%s.png", String.valueOf(m.get("type"))));
                method.setType(String.valueOf(m.get("type")));
                return method;
            }).collect(Collectors.toList());
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
    }

    public SaleOrder createOrder(ShoppingCart cart, String token, User user) {
        SaleOrder saleOrder = this.saleOrderCRUDService.initEntity();
        saleOrder.setCreatedAt(new Date());
        saleOrder.setStatus(SALE_ORDER_STATUS.PENDING);
        saleOrder.setType(SALE_ORDER_TYPE.VIRTUAL);
        saleOrder.setCreatedBy(user);
        saleOrder.setAmount(cart.getAmount());
        saleOrder.setShop(user.getShop());
        saleOrder.setAccount(user.getAccount());
        saleOrder.setPaymentManagerToken(token);
        saleOrder.setLines(new ArrayList<>());
        saleOrder.setLocale(this.locale().toLanguageTag());

        cart.getLines().forEach(l -> {
            IntStream.range(0, l.getQuantity().intValue()).forEach(i -> {
                SaleOrderLine line = new SaleOrderLine();
                line.setReturnDate(null);
                line.setBarcode(l.getProduct().getEan());
                line.setExternalCode(this.utils.generateExternalCode());
                line.setSaleOrder(saleOrder);
                line.setProduct(l.getProduct());
                line.setCreatedAt(new Date());
                line.setCreatedBy(user);

                line.setStatus(SALE_ORDER_LINE_STATUS.PENDING);
                line.setAmount(l.getAmount());

                line.setResponse(null);
                line.setResponseAux(null);
                line.setCallbackStatus(CALLBACK_STATUS.PENDING);
                
                line.setUserEmail(l.getUserEmail());
                line.setUserEmailStatus(EMAIL_STATUS.IDLE);
                
                line.setUserCellphone(l.getUserCellphone());
                line.setSmsStatus(SMS_STATUS.IDLE);

                if (Objects.nonNull(l.getStoreStep()) && !l.getStoreStep().isEmpty()) {
                    try {
                        StoreStepVO stepVO = this.utils.fromJSON(l.getStoreStep(), StoreStepVO.class);
                        line.setAreaCode(stepVO.getAreaCode());
                        line.setOperator(stepVO.getDisplayCatalog().getDisplayGroup());
                        line.setProductType(stepVO.getDisplayCatalog().getProductType());
                        line.setPhone(stepVO.getPhone());

                        EpayCatalog catalog = this.utils.fromJSON(stepVO.getProductJson(), EpayCatalog.class);
                        line.setCatalogId(catalog.getProductId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                saleOrder.getLines().add(line);
            });
        });

        return saleOrder;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void processSave(ShoppingCartVO vo, User user) {
        try {
            ShoppingCart entity = this.cartService.converter().convert(vo);
            this.fill(entity, user);
            entity.setLines(entity.getLines().stream()
                    .filter(l -> Objects.nonNull(l.getQuantity()) && l.getQuantity().signum() == 1)
                    .map(l -> {
                        this.fill(l, user);
                        l.setTotalAmount(l.getAmount().multiply(l.getQuantity()));
                        return l;
                    })
                    .collect(Collectors.toList()));
            entity.setAmount(entity.getLines().stream().map(ShoppingCartLine::getTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

            this.cartService.saveAndFlush(entity, user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException();
        }
    }

    private void fill(ShoppingCartLine entity, User user) {
        ShoppingCartLine dbLine = this.lineService.findOne(entity.getId());
        entity.setAlteredAt(new Date());
        entity.setAlteredBy(user);
        entity.setCreatedAt(dbLine.getAlteredAt());
        entity.setCreatedBy(dbLine.getCreatedBy());

        entity.setAmount(dbLine.getAmount());
        entity.setProduct(dbLine.getProduct());
    }

    private void fill(ShoppingCart entity, User user) {
        ShoppingCart dbCart = this.cartService.findOne(entity.getId());
        entity.setAlteredAt(new Date());
        entity.setAlteredBy(user);
        entity.setCreatedAt(dbCart.getAlteredAt());
        entity.setCreatedBy(dbCart.getCreatedBy());
    }

    private WSRequest createRequest(SaleOrder order) {
        WSRequest request = new WSRequest();
        request.setActivationType(ACTIVATION_TYPE.VIRTUAL);
        request.setCallbackURL(this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_CALLBACKURL));

        User user = order.getCreatedBy();

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
        request.setLines(order.getLines().stream().map(l -> {
            WSRequestLine line = new WSRequestLine();
            line.setActivationStatus(null);
            line.setBarcode(l.getBarcode());
            line.setExternalCode(l.getExternalCode());
            line.setResponse(null);
            line.setResponseAux(null);
            line.setActivationProcess(l.getProduct().getActivationProcess());

            if (l.getProduct().getActivationProcess() == ACTIVATION_PROCESS.EPAY) {
                WSRequestLineEpay epay = new WSRequestLineEpay();
                epay.setAmount(l.getAmount());
                epay.setAreaCode(l.getAreaCode());
                epay.setOperator(l.getOperator());
                epay.setPhone(l.getPhone());
                epay.setProductId(l.getCatalogId());
                line.setEpay(epay);
            }

            return line;
        }).collect(Collectors.toList()));

        return request;
    }

    public ServiceResponse activateCards(Long loggedUserId) {
        SaleOrder order = this.saleOrderCRUDService.findByCreatedByIdOrderByIdDesc(loggedUserId);
        return this.activateCards(order);
    }

    public ServiceResponse activateCards(SaleOrder order) {
        ServiceResponse result = new ServiceResponse().setResponseCode(RESPONSE_CODE.E99);
        User user = order.getCreatedBy();

        if (order.getStatus() == SALE_ORDER_STATUS.PENDING) {
            return result.setMessage("Pedido pendente! Caso tenha efetuado seu pagamento, entre em contato com o suporte.");
        } else if (order.getStatus() == SALE_ORDER_STATUS.AWAITING_PAYMENT) {
            return result.setMessage("Pedido aguardando pagamento. Assim que seu pagamento for aprovado um email será enviado lhe notificando!");
        }

        try {
            String token = this.userCRUDService.generateToken(user.getLogin());
            this.userCRUDService.setTokenForID(token, user.getId());
            WSRequest request = this.createRequest(order);

            final String endpoint = this.parameterDAO.get(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_PATH);
            WSRequest response = this.send(request, token, endpoint, user.getLogin());

            if (Objects.nonNull(response)) {
                // Resposta do WSEnterative
                RESPONSE_CODE responseCode = response.getResponse();
                if (responseCode == RESPONSE_CODE.E00) {
                    // Resposta da BHN
                    responseCode = response.getLines().get(0).getResponseAux();
                    if (Objects.isNull(responseCode) || responseCode == RESPONSE_CODE.B00) {
                        result.put("wsResponse", response).setResponseCode(RESPONSE_CODE.E00);
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

    public WSRequest send(WSRequest solicitacao, String token, String endpoint, String login) {
        ObjectMapper objectMapper = new ObjectMapper();
        WSRequest retorno = null;
        try {

            String jsonInString = objectMapper.writeValueAsString(solicitacao);

            // Leitura de Variavel de ambiente para gerar artefatos para BHN
            // String exibirJson = System.getenv("WSENTERATIVE_EXIBIR_JSON");
            String exibirJson = "SIM";
            this.log("exibirJson:" + exibirJson);
            if ("SIM".equalsIgnoreCase(exibirJson)) {
                this.log("Envio: " + jsonInString);
            }

            String retornoSTR = this.fluentService.sendWSEnterative(endpoint, jsonInString, token, login);

            if ("SIM".equalsIgnoreCase(exibirJson)) {
                this.log("Retorno: " + retornoSTR);
            }

            if (Objects.nonNull(retornoSTR)) {
                retorno = objectMapper.readValue(retornoSTR, WSRequest.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            this.log("Identificado um Time-out");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public ServiceResponse initPayment(String method, Long orderId) {
        ServiceResponse result = new ServiceResponse();
        SaleOrder order = this.saleOrderCRUDService.findOne(orderId);

        try {
            switch (method) {
                case "PAGSEGURO":
                    result = this.paymentService.initPaymentShoppingCart(method, order);
                    order.setPaymentGatewayToken(result.get("token"));
                    this.saleOrderCRUDService.saveAndFlush(order, this.systemUserId());
                    break;
                case "CIELO":
                    result = this.paymentService.initPaymentShoppingCart(method, order);
                    break;
                case "ENTERATIVE":
                    BigDecimal accountBalance = this.accountTransactionCRUDService.retrieveAccountBalance(order.getCreatedBy().getAccount().getId());
                    BigDecimal orderAmount = order.getAmount();
                    result.put("url", this.parameterDAO.get(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ENTERATIVE_CART_REDIRECT_URL));

                    if (accountBalance.compareTo(orderAmount) >= 0) {
                        result.setResponseCode(RESPONSE_CODE.E00);
                        order.setStatus(SALE_ORDER_STATUS.PAID);
                        order.setPaymentManagerToken(null);
                        order.getLines().stream().forEach(l -> {
                            this.accountTransactionCRUDService.processSaleOrderLine(l, SALE_ORDER_LINE_STATUS.PENDING, false);
                        });
                        this.saleOrderCRUDService.saveAndFlush(order, this.systemUserId());
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
    public ServiceResponse endPayment(String paymentMethod, String transactionId, User user) {
        ServiceResponse result = new ServiceResponse();
        try {
            switch (paymentMethod) {
                case "CIELO": {
                    SaleOrder order;
                    if (Objects.isNull(transactionId) || transactionId.isEmpty()) {
                        order = this.saleOrderCRUDService.findByCreatedByOrderByIdDesc(user);
                    } else {
                        order = this.saleOrderCRUDService.findOne(Long.valueOf(transactionId));
                    }
                    
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
                        
                        this.log("[endPayment][result]: %s", this.utils.toJSON(result));

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
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E00).setResponse(order);
                                break;
                            case AWAITING_PAYMENT:
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E31).setResponse(order);
                                break;
                            default:
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E26).setResponse(order);
                                break;
                        }
                    } else {
                        throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E25), false);
                    }
                }
                break;
                case "PAGSEGURO": {
                    result = this.paymentService.checkPaymentPagseguro(transactionId, user.getShop().getPaymentToken());
                    String status = result.get("status");
                    String reference = result.get("reference");

                    PagseguroTransactionResponse pagseguroResponse = this.paymentService.pagseguroFromXml(result.get("entity"));
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
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E00).setResponse(order);
                                break;
                            case AWAITING_PAYMENT:
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E31).setResponse(order);
                                break;
                            default:
                                result.setMessage(null).setResponseCode(RESPONSE_CODE.E26).setResponse(order);
                                break;
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
            if (Objects.isNull(result)) {
                result = new ServiceResponse();
            }
            String msg = this.helperConverterService.getMessage("activation.anerroroccurred_");
            result.setMessage("%s [%s]", msg, Objects.nonNull(e.getMessage()) ? e.getMessage() : e.toString());
            e.printStackTrace();
            throw new CRUDServiceException(result);
        }
        return result;
    }

    public boolean hasAdditionalSteps(Long productID) throws CRUDServiceException {
        boolean result = false;
        try {
            Product product = this.productDAO.findOne(productID);
            switch (product.getActivationProcess()) {
                case BHN:
                    result = false;
                    break;
                case EPAY:
                    result = true;
                    break;
                default:
                    throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E00));
            }
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException(new ServiceResponse().handleException(e));
        }
        return result;
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void addProduct(Long productID, User user, StoreStepVO step) throws CRUDServiceException {
        try {
            if (Objects.isNull(user.getEmail()) || user.getEmail().isEmpty()) {
                throw new CRUDServiceException(new ServiceResponse().setResponseCode(RESPONSE_CODE.E29));
            }

            step.setProduct(this.utils.fromJSON(step.getProductJson(), EpayCatalog.class));

            ShoppingCart cart = this.cartService.findByCreatedById(user.getId());

            if (Objects.isNull(cart)) {
                cart = this.cartService.initEntity();
            }

            if (Objects.isNull(cart.getLines())) {
                cart.setLines(new ArrayList<>(0));
            }

            ShoppingCartLine line = this.lineService.initEntity();
            Product product = this.productDAO.findOne(productID);

            line.setAmount(step.getProduct().getAmount());
            line.setCart(cart);
            line.setName(product.getDisplayName());
            line.setProduct(product);
            line.setQuantity(BigDecimal.ONE);
            line.setTotalAmount(step.getProduct().getAmount());
            line.setStoreStep(this.utils.toJSON(step));
            line.setUserEmail(user.getEmail());
            line.setUserCellphone("55319");

            cart.getLines().add(line);

            cart.setAlteredBy(user);
            cart.setCreatedBy(user);
            cart.setAmount(cart.getLines().stream().map(ShoppingCartLine::getTotalAmount).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));

            this.cartService.saveAndFlush(cart, user.getId());
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException(new ServiceResponse().handleException(e));
        }
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void addProduct(Long productID, User user) throws CRUDServiceException {
        try {
            if (Objects.isNull(user.getEmail()) || user.getEmail().isEmpty()) {
                ServiceResponse response = new ServiceResponse().setResponseCode(RESPONSE_CODE.E29);
                throw new CRUDServiceException(response);
            }

            ShoppingCart cart = this.cartService.findByCreatedById(user.getId());

            if (Objects.isNull(cart)) {
                cart = this.cartService.initEntity();
            }

            if (Objects.isNull(cart.getLines())) {
                cart.setLines(new ArrayList<>(0));
            }

            Predicate<ShoppingCartLine> predicate = l -> Objects.equals(l.getProduct().getId(), productID);
            if (cart.getLines().stream().anyMatch(predicate)) {
                cart.getLines().stream().filter(predicate)
                        .forEach(l -> {
                            l.setQuantity(l.getQuantity().add(BigDecimal.ONE));
                            l.setTotalAmount(l.getAmount().multiply(l.getQuantity()));
                        });
            } else {
                ShoppingCartLine line = this.lineService.initEntity();
                Product product = this.productDAO.findOne(productID);

                line.setAmount(product.getAmount());
                line.setCart(cart);
                line.setName(product.getDisplayName());
                line.setProduct(product);
                line.setTotalAmount(product.getAmount());
                line.setUserEmail(user.getEmail());

                cart.getLines().add(line);
            }

            cart.setAlteredBy(user);
            cart.setCreatedBy(user);
            cart.setAmount(cart.getLines().stream().map(ShoppingCartLine::getTotalAmount).collect(Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)));

            this.cartService.saveAndFlush(cart, user.getId());
        } catch (CRUDServiceException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException(new ServiceResponse().handleException(e));
        }
    }

    @Transactional(rollbackFor = CRUDServiceException.class)
    public void clear(User user) throws CRUDServiceException {
        try {
            ShoppingCart cart = this.cartService.findByCreatedById(user.getId());

            if (Objects.nonNull(cart)) {
                this.cartService.delete(cart);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CRUDServiceException(new ServiceResponse());
        }
    }

}
