package br.com.chart.enterative.ws.controller;

import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.*;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.WSUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Expõe servico de Ativacao
 *
 * @author Cristhiano Roberto
 */
@RestController
@RequestMapping("/paygoIntegrator")
public class PayGoIntegrator extends UserAwareComponent {

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private ShopCRUDService shopService;

    @Autowired
    private AccountCRUDService accountCRUDService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;

    @Autowired
    private PurchaseOrderCRUDService purchaseOrderService;

    @Autowired
    private PurchaseOrderLineCRUDService purchaseOrderLineService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @RequestMapping(value = "/purchage_order/activate")
    public ResponseEntity<ServiceResponse> activatePurchaseOrder(@RequestParam Long purchaseOrderId) {
        PurchaseOrder po = purchaseOrderService.dao().findOne(purchaseOrderId);

        //accountTransactionCRUDService.dao().findByPurchaseOrderLineId()
        PurchaseOrderLine purchaseOrderLine = this.purchaseOrderLineService.dao().findOne(purchaseOrderId);
        ServiceResponse response = this.purchaseOrderService.activateOrder(purchaseOrderId, this.loggedUser());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/purchase_order/create", method = RequestMethod.POST)
    public ResponseEntity<ServiceResponse> createPurchaseOrder(@RequestParam Long userId, @RequestParam BigDecimal value, @RequestParam Long shopId) {
        User user = this.userCRUDService.findOne(userId);
        Shop shop = this.shopService.findOne(shopId);
        PurchaseOrderVO purchaseOrderVO = this.purchaseOrderService.initVO(userCRUDService.converter().convert(user));
        purchaseOrderVO.setShop(shopService.converter().convert(shop));
        purchaseOrderVO.setAccount(this.accountCRUDService.converter().convert(user.getAccount()));
        this.purchaseOrderService.addLine(purchaseOrderVO);
        purchaseOrderVO.getLines().get(0).setAmount(value);
        purchaseOrderVO.getLines().get(0).setName("PIX");
        ServiceResponse response = this.purchaseOrderService.processSave(purchaseOrderVO, this.loggedUserId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/get_info")
    public ResponseEntity<WSUserInfo> getUserInfo(@RequestParam Long id, @RequestParam(required = false) String login) throws IllegalAccessException, InstantiationException {
        User user = Objects.isNull(login) || login.isEmpty() ? this.userCRUDService.findOne(id) : this.userCRUDService.findByLogin(login);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        WSUserInfo info = new WSUserInfo(user.getId(), user.getName());
        info.setShop(user.getShop().getId());
        info.setLogin(user.getLogin());
        info.setPassword(user.getPassword());
        info.setAccount(user.getAccount().getId());
        info.setThreshold(this.accountTransactionService.retrieveAccountBalance(user.getAccount().getId()));

        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
