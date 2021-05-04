package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.controller.BaseWebController;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.enums.ACCOUNT_TRANSACTION_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.event.VirtualActivationEmailEvent;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.crud.AccountCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.AccountTransactionDeadFileCRUDService;
import br.com.chart.enterative.service.crud.BHNActivationCRUDService;
import br.com.chart.enterative.service.crud.PurchaseOrderCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderLineCRUDService;
import br.com.chart.enterative.service.crud.ShopCRUDService;
import br.com.chart.enterative.service.store.ShoppingCartService;
import br.com.chart.enterative.vo.ServiceResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Catatau
 */
@Controller
public class MaintenanceController extends BaseWebController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BHNActivationCRUDService bhnActivationService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionService;

    @Autowired
    private AccountTransactionDeadFileCRUDService accountTransactionDeadFileService;

    @Autowired
    private PurchaseOrderCRUDService purchaseOrderService;

    @Autowired
    private SaleOrderCRUDService saleOrderService;

    @Autowired
    private SaleOrderLineCRUDService saleOrderLineService;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private ShopCRUDService shopService;

    private final String maintenanceForm = "admin/maintenance/form";

    @RequestMapping(path = "admin/maintenance")
    public ModelAndView admin_maintenance() {
        return this.createView();
    }

    @RequestMapping(path = "admin/maintenance/updatelastposition")
    public ModelAndView admin_maintenance_updatelastposition() {
        this.accountTransactionService.updateLastPosition();
        return this.createView();
    }

    private List<String> updatetransactions_purchaseorder(Account account) {
        final List<String> logLines = new ArrayList<>();
        List<PurchaseOrder> purchaseOrders = this.purchaseOrderService.findByAccountIdOrderByCreatedAt(account.getId());
        purchaseOrders.stream().forEach(p -> {
            p.getLines().stream().forEach(l -> {
                AccountTransaction t = this.accountTransactionService.findByPurchaseOrderLineId(l.getId());
                AccountTransactionDeadFile d = this.accountTransactionDeadFileService.findByPurchaseOrderLineId(l.getId());

                if (l.getStatus() == PURCHASE_ORDER_STATUS.ACTIVE) {
                    if (Objects.nonNull(t) && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.ACTIVE) {
                        logLines.add(String.format("Pedido de Compra [%s]; Linha [%s]; Transação [%s]", p.getId(), l.getId(), t.getId()));
                    } else if (Objects.nonNull(d) && d.getStatus() != ACCOUNT_TRANSACTION_STATUS.ACTIVE) {
                        logLines.add(String.format("Pedido de Compra [%s]; Linha [%s]; Transação [%s]", p.getId(), l.getId(), d.getId()));
                    } else if (Objects.isNull(t) && Objects.isNull(d)) {
                        logLines.add(String.format("Pedido de Compra [%s]; Linha [%s]; Sem transação", p.getId(), l.getId()));
                    }
                }
            });
        });
        logLines.add("Pedidos de Compra verificados!");
        return logLines;
    }

    private List<String> updatetransactions_transactions(Account account) {
        final List<String> logLines = new ArrayList<>(0);

        Shop shop = this.shopService.findByAccountId(account.getId());
        if (Objects.isNull(shop)) {
            logLines.add(String.format("Account [%s] não possue loja", account.getId()));
            return logLines;
        }
        List<BHNActivation> activations = this.bhnActivationService.findByShopCode(shop.getCode());
        activations.stream().forEach(activation -> {
            SaleOrderLine line = this.saleOrderLineService.findByExternalCode(activation.getExternalCode());
            if (Objects.nonNull(line)) {
                if (line.getStatus() == SALE_ORDER_LINE_STATUS.ACTIVATED && !Objects.equals(activation.getResponseCode(), "00")) {
                    logLines.add(String.format("Ativação [%s][%s]; Linha Pedido [%s][%s]", activation.getId(), activation.getResponseCode(), line.getId(), line.getStatus()));
                } else if (line.getStatus() != SALE_ORDER_LINE_STATUS.ACTIVATED && Objects.equals(activation.getResponseCode(), "00")) {
                    logLines.add(String.format("Ativação [%s][%s]; Linha Pedido [%s][%s]", activation.getId(), activation.getResponseCode(), line.getId(), line.getStatus()));
                }
            } else if (activation.getStatus() != ACTIVATION_STATUS.CANCELED) {
                logLines.add(String.format("External Code [%s] não possue linha de pedido", activation.getExternalCode()));
            }
        });
        logLines.add("Ativações verificadas!");

        return logLines;
    }

    private List<String> updatetransactions_saleorder(Account account) {
        final List<String> logLines = new ArrayList<>(0);
        List<SaleOrder> saleOrders = this.saleOrderService.findByAccountIdOrderByCreatedAt(account.getId());
        saleOrders.stream().forEach(s -> {
            s.getLines().stream().forEach(l -> {
                List<AccountTransaction> lt = this.accountTransactionService.findBySaleOrderLineId(l.getId());
                List<AccountTransactionDeadFile> ld = this.accountTransactionDeadFileService.findBySaleOrderLineId(l.getId());

                if (Objects.nonNull(lt) && !lt.isEmpty()) {
                    lt.stream().forEach(t -> {
                        if (l.getStatus() == SALE_ORDER_LINE_STATUS.ACTIVATED && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.ACTIVE) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        } else if (l.getStatus() == SALE_ORDER_LINE_STATUS.PENDING && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.PENDING) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        } else if (l.getStatus() == SALE_ORDER_LINE_STATUS.DENIED && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.CANCELED) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        }
                    });
                } else if (Objects.nonNull(ld) && !ld.isEmpty()) {
                    ld.stream().forEach(t -> {
                        if (l.getStatus() == SALE_ORDER_LINE_STATUS.ACTIVATED && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.ACTIVE) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        } else if (l.getStatus() == SALE_ORDER_LINE_STATUS.PENDING && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.PENDING) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        } else if (l.getStatus() == SALE_ORDER_LINE_STATUS.DENIED && t.getStatus() != ACCOUNT_TRANSACTION_STATUS.CANCELED) {
                            logLines.add(String.format("Pedido de Venda [%s]; Linha [%s][%s]; Transação [%s][%s]", s.getId(), l.getId(), l.getStatus(), t.getId(), t.getStatus()));
                        }
                    });
                } else if (s.getStatus() != SALE_ORDER_STATUS.DENIED) {
                    logLines.add(String.format("Pedido de Venda [%s]; Linha [%s]; Sem transação", s.getId(), l.getId()));
                }
            });
        });
        logLines.add("Pedidos de Venda verificados!");
        return logLines;
    }

    @RequestMapping(path = "admin/maintenance/processsaleorderline")
    public ModelAndView admin_maintenance_processsaleorderline(SaleOrderLine line) {
        SALE_ORDER_LINE_STATUS lineStatus;
        SaleOrderLine tmpLine = this.saleOrderLineService.findOne(line.getId());
        
        if (Objects.isNull(line.getStatus())) {            
            lineStatus = this.saleOrderService.retrieveLineStatusWithResponse(tmpLine, RESPONSE_CODE.E00, tmpLine.getResponseAux(), tmpLine.getActivationStatus());
        } else {
            lineStatus = line.getStatus();
        }
        
        tmpLine.setStatus(lineStatus);

        ServiceResponse response = this.accountTransactionService.processSaleOrderLine(tmpLine, lineStatus, false);
        if (Objects.nonNull(response.getMessage())) {
            throw new CRUDServiceException(response);
        }
        tmpLine = this.saleOrderLineService.saveAndFlush(tmpLine, this.systemUserId());

        SALE_ORDER_STATUS orderStatus = this.saleOrderService.retrieveOrderStatus(tmpLine.getSaleOrder());
        this.saleOrderService.setStatusForId(orderStatus, tmpLine.getSaleOrder().getId());

        ModelAndView mv = this.createView();
        mv.addObject("log", "Linha do pedido processada!");
        return mv;
    }

    private void resendemail(Long id) {
        this.applicationEventPublisher.publishEvent(new VirtualActivationEmailEvent(this, id));
    }

    @RequestMapping(path = "admin/maintenance/resendemail")
    public ModelAndView admin_maintenance_resendemail(SaleOrder order) {
        final List<String> logLines = new ArrayList<>(0);
        if (Objects.nonNull(order) && Objects.nonNull(order.getId())) {
            this.resendemail(order.getId());
        } else {
            List<SaleOrder> orders = this.saleOrderService.findByEmailStatusAndTypeAndStatus(EMAIL_STATUS.IDLE, SALE_ORDER_TYPE.VIRTUAL, SALE_ORDER_STATUS.ACTIVATED);
            orders.stream().map(SaleOrder::getId).forEach(this::resendemail);
        }
        ModelAndView mv = this.createView();
        mv.addObject("log", "Email enviados!");
        return mv;
    }

    @RequestMapping(path = "admin/maintenance/updatetransactions")
    public ModelAndView admin_maintenance_updatetransactions(Account account) {

        final List<String> logLines = new ArrayList<>(0);
        if (Objects.nonNull(account) && Objects.nonNull(account.getId())) {
            logLines.add("<p>");
            Account a = this.accountService.findOne(account.getId());
            
            logLines.add("Conta: " + a.getName());
            this.log("[updatetransactions] Purchase Order");
            logLines.addAll(this.updatetransactions_purchaseorder(account));
            this.log("[updatetransactions] Sale Order");
            logLines.addAll(this.updatetransactions_saleorder(account));
            this.log("[updatetransactions] Transactions");
            logLines.addAll(this.updatetransactions_transactions(account));
            logLines.add("</p>");
        } else {
            this.accountService.findAll().forEach(ac -> {
                logLines.add("<p>");
                this.log("Conta: " + ac.getName());
                logLines.add(String.format("Account [%s]", ac.getName()));
                this.log("[updatetransactions] Purchase Order");
                logLines.addAll(this.updatetransactions_purchaseorder(ac));
                this.log("[updatetransactions] Sale Order");
                logLines.addAll(this.updatetransactions_saleorder(ac));
                this.log("[updatetransactions] Transactions");
                logLines.addAll(this.updatetransactions_transactions(ac));
                logLines.add("</p>");
            });
        }

        ModelAndView mv = this.createView();
        mv.addObject("log", logLines.stream().collect(Collectors.joining("<br />")));
        return mv;
    }

    @RequestMapping(path = "admin/maintenance/activatepaid")
    public ModelAndView admin_maintenance_activatepaid(SaleOrder order) {
        final List<String> logLines = new ArrayList<>();

        if (Objects.nonNull(order) && Objects.nonNull(order.getId())) {
            order = this.saleOrderService.findOne(order.getId());
            this.shoppingCartService.activateCards(order);
        } else {
            List<SaleOrder> orders = this.saleOrderService.findByStatus(SALE_ORDER_STATUS.PAID);
            orders.forEach(this.shoppingCartService::activateCards);
        }

        ModelAndView mv = this.createView();
        mv.addObject("log", String.join("<br />", logLines));
        return mv;
    }

    @RequestMapping(path = "admin/maintenance/balancethreshold")
    public ModelAndView admin_maintenance_balancethreshold(Account account) {
        final List<String> logLines = new ArrayList<>();

        if (Objects.nonNull(account) && Objects.nonNull(account.getBalanceThreshold())) {
            BigDecimal balanceThreshold = account.getBalanceThreshold();
            this.accountService.findAll().forEach(ac -> {
                this.accountService.setBalanceThresholdForID(balanceThreshold, ac.getId());
                logLines.add(String.format("Updated account [%s][%s]", ac.getId(), ac.getName()));
            });
        }

        ModelAndView mv = this.createView();
        mv.addObject("log", String.join("<br />", logLines));
        return mv;
    }

    public ModelAndView createView() {
        ModelAndView mv = super.createView(this.maintenanceForm);
        mv.addObject("account_list", this.accountService.findByStatusOrderByName(STATUS.ACTIVE));
        mv.addObject("account", new Account());
        mv.addObject("saleorderline", new SaleOrderLine());
        mv.addObject("saleorder", new SaleOrder());
        mv.addObject("saleorderline_status", SALE_ORDER_LINE_STATUS.ordered());
        return mv;
    }

}
