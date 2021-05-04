package br.com.chart.enterative.service.activation;

import br.com.chart.enterative.cron.EpayDefaultTransactionListener;
import br.com.chart.enterative.cron.EpayTransactionThread;
import br.com.chart.enterative.cron.EpayUpdateTransactionListener;
import br.com.chart.enterative.dao.EpayActivationDAO;
import br.com.chart.enterative.dao.EpayTransactionDAO;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.entity.EpayTransaction;
import br.com.chart.enterative.entity.EpayVoucher;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.request.EpayRequestService;
import br.com.chart.enterative.vo.epay.EpayTransactionThreadVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class EpayQueueActivationService extends UserAwareComponent {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private EpayActivationDAO activationDAO;

    @Autowired
    private EpayTransactionDAO transactionDAO;

    @Autowired
    private EpayRequestService requestService;

    public EpayTransactionThread initThread(String name) {
        EpayTransactionThread thread = new EpayTransactionThread(name);
        this.beanFactory.autowireBean(thread);
        return thread;
    }

    public List<EpayTransactionThreadVO> retrieveActivations() {
        final List<EpayTransactionThreadVO> result = new ArrayList<>(0);

        List<EpayActivation> activations = this.activationDAO.findByStatusInAndQueueStatus(new ACTIVATION_STATUS[]{ACTIVATION_STATUS.ACTIVATION, ACTIVATION_STATUS.INCOMPLETE, ACTIVATION_STATUS.REVERSAL}, ACTIVATION_QUEUE_STATUS.IDLE);
        activations.forEach(activation -> {
            EpayVoucher voucher = activation.getVoucher();
            EpayTransaction transaction = this.requestService.initTransaction(activation);

            if (Objects.nonNull(transaction)) {
                transaction.setActivation(activation);
                transaction.setCreatedAt(new Date());
                transaction.setQueueInsertDate(new Date());
                transaction.setDirection(TRANSACTION_DIRECTION.DISPATCH);
                this.transactionDAO.saveAndFlush(transaction, this.systemUserId());

                activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.QUEUED);
                this.activationDAO.saveAndFlush(activation, this.systemUserId());

                result.add(this.initVO(transaction, false));
            }
        });

        return result;
    }

    public EpayTransactionThreadVO initVO(EpayTransaction transaction, boolean reversal) {
        EpayTransactionThreadVO vo = new EpayTransactionThreadVO(transaction.getId(), transaction);
        vo.addCallback(new EpayDefaultTransactionListener());

        EpayUpdateTransactionListener listener = new EpayUpdateTransactionListener();
        beanFactory.autowireBean(listener);
        vo.addCallback(listener);

        return vo;
    }
}
