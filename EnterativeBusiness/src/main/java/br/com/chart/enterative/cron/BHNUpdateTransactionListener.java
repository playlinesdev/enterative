package br.com.chart.enterative.cron;

import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.BHNTransactionDAO;
import br.com.chart.enterative.dao.BHNVoucherDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.HARD;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.SOFT;
import static br.com.chart.enterative.enums.ACTIVATION_DECISION.TIMEOUT;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.event.CallbackEvent;
import br.com.chart.enterative.event.BHNTransactionThreadEvent;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.request.BHNRequestService;
import br.com.chart.enterative.vo.bhn.BHNRequestTransaction;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import br.com.chart.enterative.interfaces.BHNTransactionThreadListener;

/**
 *
 * @author William Leite
 */
public class BHNUpdateTransactionListener extends UserAwareComponent implements BHNTransactionThreadListener {

    @Autowired
    private BHNActivationDAO bhnActivationDAO;

    @Autowired
    private BHNTransactionDAO bhnTransactionDAO;

    @Autowired
    private BHNVoucherDAO bhnVoucherDAO;

    @Autowired
    private BHNRequestService bhnRequestContainerService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void notify(BHNTransactionThreadEvent event) {
        ACTIVATION_DECISION decisao = event.getDecisao();

        //Transacao transacaoOriginal = transacaoRepository.findOne(evento.getControle().getTransacao().getIdtransacao());
        BHNTransaction transacaoOriginal = event.getVo().getBhnTransaction();

        BHNActivation activation = transacaoOriginal.getBhnActivation();
        activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.PROCESSED);

        String responseCode = null;
        BHNTransaction transacaoRetorno = null;

        if (decisao != ACTIVATION_DECISION.TIMEOUT) {
            if (Objects.nonNull(event.getResponseContainer())) {
                responseCode = event.getResponseContainer().getResponse().getTransaction().getResponseCode();

                transacaoOriginal.setResponseCode(responseCode);
                transacaoOriginal.setTransactionReturnDate(new Date());

                transacaoRetorno = this.bhnRequestContainerService.convert(event.getResponseContainer().getResponse());

                transacaoRetorno.setDirection(TRANSACTION_DIRECTION.RETURN);
                transacaoRetorno.setBhnActivation(transacaoOriginal.getBhnActivation());
                transacaoRetorno.setCreatedAt(transacaoOriginal.getCreatedAt());
                transacaoRetorno.setQueueInsertDate(transacaoOriginal.getQueueInsertDate());
                transacaoRetorno.setTransactionReturnDate(transacaoOriginal.getTransactionReturnDate());
                transacaoRetorno.setResource(transacaoOriginal.getResource());
            }

            if (decisao != ACTIVATION_DECISION.NONPROCESSED && !event.getVo().isReversal()) {
                activation.setResponseCode(responseCode);
            }
        }

        if (event.getVo().isReversal()) {
            activation.setReversalSentDate(new Date());
        }

        switch (decisao) {
            case APPROVED:
                if (event.getVo().isReversal()) {
                    activation.setStatus(ACTIVATION_STATUS.ACTIVATION);
                    int ttl = activation.getTtlActivation() - 1;
                    activation.setTtlActivation(ttl);
                } else {
                    BHNRequestTransaction transaction = event.getResponseContainer().getResponse().getTransaction();
                    if (Objects.nonNull(transaction) && Objects.nonNull(transaction.getAdditionalTxnFields())) {
                        String pin = transaction.getAdditionalTxnFields().getRedemptionPin();
                        if (Objects.isNull(pin)) {
                            pin = transaction.getAdditionalTxnFields().getRedemptionAccountNumber();
                        }
                        BHNVoucher voucher = activation.getVoucher();
                        voucher.setPin(pin);
                        this.bhnVoucherDAO.saveAndFlush(voucher, this.systemUserId());
                    }
                    activation.setStatus(ACTIVATION_STATUS.PROCESSED);
                }
                break;
            case SOFT:
            case TIMEOUT:
                activation.setStatus(ACTIVATION_STATUS.REVERSAL);
                activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.IDLE);

                if (Objects.isNull(responseCode)) {
                    //responseCode = "C99";
                    responseCode = "E09"; // Timeout
                }
                activation.setResponseCode(responseCode);

                if (event.getVo().isReversal()) {
                    int ttl = activation.getTtlReversal() - 1;
                    activation.setTtlReversal(ttl);
                }
                break;
            case HARD:
                activation.setStatus(ACTIVATION_STATUS.PROCESSED);
                break;
            case NONPROCESSED:
                activation.setStatus(ACTIVATION_STATUS.NONPROCESSED);
                break;
        }
        if (Objects.nonNull(activation)) {
            activation.setCallbackStatus(CALLBACK_STATUS.PENDING);
            this.bhnActivationDAO.saveAndFlush(activation, this.systemUserId());
            this.applicationEventPublisher.publishEvent(new CallbackEvent(this, activation));
        }
        if (Objects.nonNull(transacaoOriginal)) {
            this.bhnTransactionDAO.saveAndFlush(transacaoOriginal, this.systemUserId());
        }
        if (Objects.nonNull(transacaoRetorno)) {
            this.bhnTransactionDAO.saveAndFlush(transacaoRetorno, this.systemUserId());
        }
    }
}
