package br.com.chart.enterative.cron;

import br.com.chart.enterative.dao.EpayActivationDAO;
import br.com.chart.enterative.dao.EpayTransactionDAO;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.EpayTransaction;
import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.HARD;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.SOFT;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.TIMEOUT;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.event.CallbackEvent;
import br.com.chart.enterative.event.EpayTransactionThreadEvent;
import br.com.chart.enterative.interfaces.EpayTransactionThreadListener;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.request.EpayRequestService;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 *
 * @author William Leite
 */
public class EpayUpdateTransactionListener extends UserAwareComponent implements EpayTransactionThreadListener {
    @Autowired
    private EpayTransactionDAO transactionDAO;
    
    @Autowired
    private EpayActivationDAO activationDAO;

    @Autowired
    private EpayRequestService requestService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void notify(EpayTransactionThreadEvent event) {
        ACTIVATION_DECISION decisao = event.getDecisao();

        EpayTransaction originalTransaction = event.getVo().getTransaction();

        EpayActivation activation = originalTransaction.getActivation();
        activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.PROCESSED);

        String result = null;
        EpayTransaction transacaoRetorno = null;

        if (decisao != ACTIVATION_DECISION.TIMEOUT) {
            if (Objects.nonNull(event.getResponseContainer())) {
                result = event.getResponseContainer().getResult();

                originalTransaction.setResultCode(result);
                originalTransaction.setTransactionReturnDate(new Date());

                transacaoRetorno = this.requestService.convert(event.getResponseContainer());

                transacaoRetorno.setDirection(TRANSACTION_DIRECTION.RETURN);
                transacaoRetorno.setActivation(originalTransaction.getActivation());
                transacaoRetorno.setCreatedAt(originalTransaction.getCreatedAt());
                transacaoRetorno.setQueueInsertDate(originalTransaction.getQueueInsertDate());
                transacaoRetorno.setTransactionReturnDate(originalTransaction.getTransactionReturnDate());
                transacaoRetorno.setResource(originalTransaction.getResource());
            }

            if (decisao != ACTIVATION_DECISION.NONPROCESSED) {
                activation.setResponseCode(result);
            }
        }

        switch (decisao) {
            case APPROVED:
                activation.setStatus(ACTIVATION_STATUS.PROCESSED);
                break;
            case INCOMPLETE:
                activation.setStatus(ACTIVATION_STATUS.INCOMPLETE);
                activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.IDLE);
                break;
            case SOFT:
            case TIMEOUT:
            case HARD:
                activation.setStatus(ACTIVATION_STATUS.REVERSAL);
                activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.IDLE);
                break;
            case NONPROCESSED:
                activation.setStatus(ACTIVATION_STATUS.NONPROCESSED);
                break;
        }

        if (Objects.nonNull(activation)) {
            // On incomplete activations we don't notify the callback
            if (activation.getStatus() != ACTIVATION_STATUS.INCOMPLETE) {
                activation.setCallbackStatus(CALLBACK_STATUS.PENDING);
                this.applicationEventPublisher.publishEvent(new CallbackEvent(this, activation));
            }
            this.activationDAO.saveAndFlush(activation, this.systemUserId());
        }

        if (Objects.nonNull(originalTransaction)) {
            this.transactionDAO.saveAndFlush(originalTransaction, this.systemUserId());
        }
        if (Objects.nonNull(transacaoRetorno)) {
            this.transactionDAO.saveAndFlush(transacaoRetorno, this.systemUserId());
        }
    }
}
