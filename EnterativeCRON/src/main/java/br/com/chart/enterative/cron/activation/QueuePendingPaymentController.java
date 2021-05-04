package br.com.chart.enterative.cron.activation;

import br.com.chart.enterative.dao.SaleOrderDAO;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.service.activation.WebActivationService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.store.ShoppingCartService;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
@Component
public class QueuePendingPaymentController extends UserAwareComponent {

    @Autowired
    private SaleOrderDAO saleOrderDAO;

    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private WebActivationService webActivationService;

    @Transactional
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void checkPendingPayment() {
        this.log("-- Checking Pending Payment");
        List<SaleOrder> saleOrders = this.saleOrderDAO.findByStatusEager(SALE_ORDER_STATUS.AWAITING_PAYMENT);
        this.log("-- Orders found: " + saleOrders.size());
        
        saleOrders.forEach(order -> {
            this.log("-- Payment ---");
            this.log("Order: %s [%s]", order.getId(), order.getType());
            
            ServiceResponse response;
            if (Objects.nonNull(order.getPaymentManagerToken())) {
                String type;
                String transactionId;
                if (Objects.nonNull(order.getPaymentTransactionId()) && !order.getPaymentTransactionId().isEmpty()) {
                    type = "PAGSEGURO";
                    transactionId = order.getPaymentTransactionId();
                } else {
                    type = "CIELO";
                    transactionId = order.getId().toString();
                }

                User user = this.userDAO.findOne(order.getCreatedBy().getId());

                if (order.getType() == SALE_ORDER_TYPE.VIRTUAL) {
                    response = this.cartService.endPayment(type, transactionId, user);
                } else {
                    response = this.webActivationService.endPayment(type, transactionId, user);
                }

                if (response.getResponseCode() == RESPONSE_CODE.E00) {
                    order = (SaleOrder) response.getResponse();
                    order = this.saleOrderDAO.findByIdEager(order.getId());
                    if (order.getType() == SALE_ORDER_TYPE.VIRTUAL) {
                        response = this.cartService.activateCards(order);
                    } else {
                        response = this.webActivationService.activate(order, user);
                    }
                    if (response.getResponseCode() != RESPONSE_CODE.E00) {
                        order.setStatus(SALE_ORDER_STATUS.AWAITING_PAYMENT);
                        this.saleOrderDAO.saveAndFlush(order, parameterDAO.get(ENVIRONMENT_PARAMETER.SYSTEM_USER));
                        this.log("Back in line");
                    } else {
                        this.log("Successfully added to queue");
                    }
                } else {
                    this.log("No change");
                }
            }
            
            this.log("-- End Payment --");
        });
    }
}
