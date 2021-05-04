package br.com.chart.enterative.ws.controller;

import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.vo.ProductHighlightVO;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.entity.vo.ShoppingCartVO;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.CallbackQueueService;
import br.com.chart.enterative.service.activation.WebActivationService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.BHNActivationCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.store.ShoppingCartService;
import br.com.chart.enterative.service.store.StoreService;
import br.com.chart.enterative.vo.CartaoVO;
import br.com.chart.enterative.vo.SaleOrderReceiptVO;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.ws.model.FlutterProduct;
import br.com.chart.enterative.ws.model.WSResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flutter")
public class FlutterController extends UserAwareComponent {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private SaleOrderCRUDService saleOrderService;

    @Autowired
    private CallbackQueueService callbackService;

    @Autowired
    private BHNActivationCRUDService bhnActivationService;

    @Autowired
    private WebActivationService ativacaoService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping(path = "login")
    public HttpEntity<WSResponse<Boolean>> login() {
        final String method = "login";
        return ResponseEntity.ok(WSResponse.of(Boolean.TRUE, method));
    }

    @GetMapping(path = "store/favorites")
    public HttpEntity<WSResponse<List<ProductVO>>> store_favorites() {
        final String method = "store/favorites";
        try {
            List<ProductVO> favorites = this.storeService.retrieveFavorites(this.loggedUserId());
            return ResponseEntity.ok(WSResponse.of(favorites, method));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @PutMapping(path = "store/favorites/{id}")
    public HttpEntity<WSResponse<Boolean>> store_favorites_id(@PathVariable("id") Long id, Locale locale) {
        final String method = "store/favorites/{id}";
        try {
            this.storeService.toggleFavorite(id, this.loggedUserId());
            return ResponseEntity.ok(WSResponse.of(Boolean.TRUE, method));
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @GetMapping(path = "store/highlights")
    public HttpEntity<WSResponse<List<ProductHighlightVO>>> store_highlights() {
        final String method = "store/highlights";
        try {
            List<ProductHighlightVO> highlights = this.storeService.retrieveHighlights(this.loggedUserId());
            return ResponseEntity.ok(WSResponse.of(highlights, method));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @GetMapping(path = "store/products")
    public HttpEntity<WSResponse<List<FlutterProduct>>> store_products() {
        final String method = "store/products";
        System.out.println(method);
        try {
            List<ProductHighlightVO> highlights = this.storeService.retrieveHighlights(this.loggedUserId());
            List<ProductVO> favorites = this.storeService.retrieveFavorites(this.loggedUserId());
            List<FlutterProduct> products = this.storeService.retrieveProducts(this.loggedUserId(), PRODUCT_TYPE.VIRTUAL)
                    .stream().map(FlutterProduct::fromVO).peek(fp -> {
                        fp.setHighlight(highlights.stream().anyMatch(h -> Objects.equals(h.getProduct().getId(), fp.getId())));
                        fp.setFavorite(favorites.stream().anyMatch(f -> Objects.equals(f.getId(), fp.getId())));
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(WSResponse.of(products, method));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @GetMapping(path = "store/products/cached")
    public HttpEntity<WSResponse<List<FlutterProduct>>> store_products_cached() {
        final String method = "store/products/cached";
        System.out.println(method);
        try {
            List<ProductHighlightVO> highlights = this.storeService.retrieveHighlights(this.loggedUserId());
            List<ProductVO> favorites = this.storeService.retrieveFavorites(this.loggedUserId());
            List<FlutterProduct> products = this.storeService.retrieveProductsWithoutImage(this.loggedUserId(), PRODUCT_TYPE.VIRTUAL)
                    .stream().map(FlutterProduct::fromVO).peek(fp -> {
                        fp.setHighlight(highlights.stream().anyMatch(h -> Objects.equals(h.getProduct().getId(), fp.getId())));
                        fp.setFavorite(favorites.stream().anyMatch(f -> Objects.equals(f.getId(), fp.getId())));
                        fp.setImagem(null);
                    }).collect(Collectors.toList());
            return ResponseEntity.ok(WSResponse.of(products, method));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @GetMapping(path = "store/details/{id}")
    public HttpEntity<WSResponse<ProductVO>> store_details_id(@PathVariable("id") Long id) {
        final String method = "store/details/{id}";
        try {
            return ResponseEntity.ok(WSResponse.of(this.storeService.retrieveProduct(id, this.loggedUserId()), method));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @GetMapping(path = "cart")
    public HttpEntity<WSResponse<ShoppingCartVO>> cart(Locale locale) {
        final String method = "cart";
        try {
            ServiceResponse response = this.shoppingCartService.retrieveCart(this.loggedUserId());
            ShoppingCartVO cart = response.get("cart");
            return ResponseEntity.ok(WSResponse.of(cart, method));
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @PostMapping(path = "cart")
    public HttpEntity<WSResponse<ShoppingCartVO>> cart(@RequestBody ShoppingCartVO cart, Locale locale) {
        final String method = "cart";
        try {
            this.shoppingCartService.processSave(cart, this.loggedUser());
            return this.cart(locale);
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @GetMapping(path = "cart/count")
    public HttpEntity<WSResponse<Integer>> cart_count(Locale locale) {
        final String method = "cart/count";
        try {
            ServiceResponse response = this.shoppingCartService.retrieveCart(this.loggedUserId());
            ShoppingCartVO cart = response.get("cart");
            if (Objects.nonNull(cart)) {
                return ResponseEntity.ok(WSResponse.of(cart.getLines().size(), method));
            } else {
                return ResponseEntity.ok(WSResponse.of(0, method));
            }
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @PostMapping(path = "cart/add/{id}")
    public HttpEntity<WSResponse<Boolean>> cart_add_id(@PathVariable("id") Long id, Locale locale) {
        final String method = "cart/add/{id}";
        try {
            this.shoppingCartService.addProduct(id, this.loggedUser());
            return ResponseEntity.ok(WSResponse.of(Boolean.TRUE, method));
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(Boolean.FALSE, message, method));
        }
    }

    @PostMapping(path = "cart/conclude")
    public HttpEntity<WSResponse<Long>> cart_conclude(Locale locale) {
        final String method = "cart/conclude";
        try {
            ServiceResponse response = this.shoppingCartService.preparePayment(this.loggedUser());
            if (response.getResponseCode() == RESPONSE_CODE.E00) {
                Long orderId = response.get("orderId");
                response = this.shoppingCartService.initPayment("ENTERATIVE", orderId);
                if (response.getResponseCode() == RESPONSE_CODE.E00) {
                    response = this.shoppingCartService.activateCards(loggedUserId());
                    if (response.getResponseCode() == RESPONSE_CODE.E00) {
                        this.shoppingCartService.clear(loggedUser());
                        return ResponseEntity.ok(WSResponse.of(orderId, method));
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, response.getMessage(), method));
        } catch (CRUDServiceException e) {
            e.printStackTrace();
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @GetMapping(path = "saleorder/callbackstatus/{id}")
    public HttpEntity<WSResponse<Boolean>> saleorder_callbackstatus_id(@PathVariable("id") Long id, Locale locale) {
        final String method = "saleorder/callbackstatus/{id}";
        try {
            Boolean result = StringUtils.equalsIgnoreCase(this.callbackService.checkCallback(id), "OK");
            return ResponseEntity.ok(WSResponse.of(result, method));
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @GetMapping(path = "saleorder/{id}")
    public HttpEntity<WSResponse<SaleOrderVO>> saleorder_id(@PathVariable("id") Long id, Locale locale) {
        final String method = "saleorder/{id}";
        try {
            SaleOrder saleOrder = this.saleOrderService.findOne(id);
            SaleOrderVO vo = this.saleOrderService.converter().convert(saleOrder);
            vo.setTranslatedType(this.messageSource.getMessage(vo.getType().getDescription(), null, locale));
            vo.setTranslatedStatus(this.messageSource.getMessage(vo.getStatus().getDescription(), null, locale));

            vo.getLines().stream().forEach(l -> {
                l.setTranslatedStatus(this.messageSource.getMessage(l.getStatus().getDescription(), null, locale));
                l.setTranslatedActivationStatus(this.messageSource.getMessage(l.getActivationStatus().getDescription(), null, locale));
                if (Objects.nonNull(l.getResponse()))
                    l.setTranslatedResponse(l.getResponse().getDescription());
                if (Objects.nonNull(l.getResponseAux()))
                    l.setTranslatedResponseAux(l.getResponseAux().getDescription());
            });

            return ResponseEntity.ok(WSResponse.of(vo, method));
        } catch (CRUDServiceException e) {
            String message = this.messageSource.getMessage(e.getMessage(), null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, message, method));
        }
    }

    @GetMapping(path = "saleorder/receipt/{id}")
    public HttpEntity<WSResponse<List<SaleOrderReceiptVO>>> saleorder_receipt_id(@PathVariable("id") Long id, Locale locale) {
        final String method = "saleorder/receipt/{id}";
        try {
            SaleOrder saleOrder = this.saleOrderService.findOne(id);

            Merchant merchant = saleOrder.getShop().getMerchant();
            String terminal = saleOrder.getCreatedBy().getTerminal();
            String shopCode = saleOrder.getShop().getCode();

            Integer receiptCount = Optional.ofNullable(saleOrder.getReceiptCount()).orElse(0);
            if (receiptCount == 0) {
                List<SaleOrderReceiptVO> result = saleOrder.getLines().stream().map(line -> {
                    SaleOrderReceiptVO vo = new SaleOrderReceiptVO();
                    vo.setAmount(line.getAmount());
                    vo.setDate(saleOrder.getCreatedAt());
                    vo.setProduct(line.getProduct().getDisplayName());
                    vo.setStatus(this.messageSource.getMessage(saleOrder.getStatus().getDescription(), null, locale));
                    vo.setType(this.messageSource.getMessage(saleOrder.getType().getDescription(), null, locale));

                    BHNActivation activation = this.bhnActivationService.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, line.getExternalCode());
                    if (Objects.nonNull(activation)) {
                        vo.setPin(activation.getVoucher().getPin());
                    } else {
                        vo.setPin("--");
                    }
                    return vo;
                }).collect(Collectors.toList());

                saleOrder.setReceiptCount(++receiptCount);
                this.saleOrderService.saveAndFlush(saleOrder, this.systemUserId());
                return ResponseEntity.ok(WSResponse.of(result, method));
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, method));
        }
    }

    @PostMapping(path = "card/{barcode}")
    public HttpEntity<WSResponse<CartaoVO>> card_barcode(@PathVariable("barcode") String barcode, Locale locale) {
        final String method = "card/{barcode}";

        CartaoVO cartao = new CartaoVO();
        cartao.setBarcode(barcode);

        ServiceResponse response = this.ativacaoService.createActivationView_SecondStep(cartao, loggedUser());
        if (StringUtils.isBlank(response.getMessage())) {
            return ResponseEntity.ok(WSResponse.of(cartao, method));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, response.getMessage(), method));
    }

    @PostMapping(path = "card/activate")
    public HttpEntity<WSResponse<Long>> card_activate(@RequestBody CartaoVO cartao) {
        final String method = "card/activate";

        ServiceResponse result = this.ativacaoService.activateByBalance(cartao, loggedUser());
        if (Objects.isNull(result.getMessage())) {
            SaleOrder order = result.get("pedido");
            return ResponseEntity.ok(WSResponse.of(order.getId(), method));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(WSResponse.of(null, result.getMessage(), method));
    }
}