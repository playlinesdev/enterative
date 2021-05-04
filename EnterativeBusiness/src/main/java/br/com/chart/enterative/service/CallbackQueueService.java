package br.com.chart.enterative.service;

import br.com.chart.enterative.cron.CallbackThread;
import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.CallbackQueueDAO;
import br.com.chart.enterative.dao.EpayActivationDAO;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.entity.CallbackQueue;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.event.PersonallyActivationEmailEvent;
import br.com.chart.enterative.event.VirtualActivationEmailEvent;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.AccountTransactionCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.vo.CallbackDataVO;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CallbackQueueService extends UserAwareComponent {

    @Autowired
    private SaleOrderLineDAO saleOrderLineDAO;

    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;

    @Autowired
    private AccountTransactionCRUDService accountTransactionCRUDService;

    @Autowired
    private BHNActivationDAO bhnActivationDAO;
    
    @Autowired
    private EpayActivationDAO epayActivationDAO;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CallbackQueueDAO callbackQueueDAO;

    public HttpStatus receiveCallback(CallbackDataVO vo) {
        HttpStatus result;
        SaleOrderLine line = this.saleOrderLineDAO.findByExternalCode(vo.getExternalCode());
        try {
            if (Objects.nonNull(line)) {
                if (line.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN) {
                    line.setResponseAux(RESPONSE_CODE.get(vo.getResponseCode()));
                } else if (line.getProduct().getActivationProcess() == ACTIVATION_PROCESS.EPAY) {
                    line.setResponseAux(RESPONSE_CODE.get(String.format("P%s", vo.getResponseCode())));
                }
                
                line.setReturnDate(vo.getLastUpdate());
                line.setCallbackStatus(CALLBACK_STATUS.DONE);
                line.setActivationStatus(vo.getActivationStatus());

                // On callback, responseCode is always E00
                SALE_ORDER_LINE_STATUS lineStatus = this.saleOrderCRUDService.retrieveLineStatusWithResponse(line, RESPONSE_CODE.E00, line.getResponseAux(), line.getActivationStatus());
                line.setStatus(lineStatus);

                ServiceResponse response = this.accountTransactionCRUDService.processSaleOrderLine(line, lineStatus, true);
                if (Objects.nonNull(response.getMessage())) {
                    throw new IllegalArgumentException(response.getMessage());
                }

                line = this.saleOrderLineDAO.saveAndFlush(line, this.systemUserId());

                SALE_ORDER_STATUS orderStatus = this.saleOrderCRUDService.retrieveOrderStatus(line.getSaleOrder());
                this.saleOrderCRUDService.setStatusForId(orderStatus, line.getSaleOrder().getId());

                if (orderStatus == SALE_ORDER_STATUS.ACTIVATED) {
                    if (line.getSaleOrder().getType() == SALE_ORDER_TYPE.VIRTUAL) {
                        this.applicationEventPublisher.publishEvent(new VirtualActivationEmailEvent(this, line.getSaleOrder().getId()));
                    } else if (line.getSaleOrder().getType() == SALE_ORDER_TYPE.PERSONALLY) {
                        this.applicationEventPublisher.publishEvent(new PersonallyActivationEmailEvent(this, line.getSaleOrder().getId()));
                    }
                }
                result = HttpStatus.OK;
            } else {
                this.log("Callback não encontrou pedido: " + vo.getExternalCode());
                result = HttpStatus.UNPROCESSABLE_ENTITY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return result;
    }

    public String checkCallback(SaleOrder saleOrder) {
        String result = "";
        if (Objects.nonNull(saleOrder)) {
            boolean isOK = saleOrder.getLines().stream().allMatch(i -> i.getCallbackStatus() == CALLBACK_STATUS.DONE);
            if (isOK) {
                result = "OK";
            }
        } else {
            this.log("[CheckCallback] Nenhum pedido encontrado! " + saleOrder);
            result = "error";
        }
        return result;
    }

    public String checkCallback(Long id) {
        SaleOrder saleOrder = this.saleOrderCRUDService.findOne(id);
        return this.checkCallback(saleOrder);
    }

    public List<CallbackQueue> retrieveQueue() {
        final List<CallbackQueue> result = new ArrayList<>();
        try {
            Stream<CallbackQueue> callbacks = this.callbackQueueDAO.findAll();
            callbacks.forEach(c -> {
                result.add(c);
                
                if (Objects.nonNull(c.getBhnActivation())) {
                    this.bhnActivationDAO.setCallbackStatusForID(CALLBACK_STATUS.WAITING, c.getBhnActivation().getId());
                } else if (Objects.nonNull(c.getEpayActivation())) {
                    this.epayActivationDAO.setCallbackStatusForID(CALLBACK_STATUS.WAITING, c.getEpayActivation().getId());
                }
                
                this.callbackQueueDAO.delete(c);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public CallbackThread initThread() {
        CallbackThread thread = new CallbackThread();
        this.beanFactory.autowireBean(thread);
        return thread;
    }
}
