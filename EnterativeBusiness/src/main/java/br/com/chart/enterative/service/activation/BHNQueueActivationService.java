package br.com.chart.enterative.service.activation;

import br.com.chart.enterative.cron.BHNDefaultTransactionListener;
import br.com.chart.enterative.cron.BHNTransactionThread;
import br.com.chart.enterative.cron.BHNUpdateTransactionListener;
import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.BHNTransactionDAO;
import br.com.chart.enterative.dao.ResourceDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.request.BHNRequestService;
import br.com.chart.enterative.vo.bhn.BHNRequestContainer;
import br.com.chart.enterative.vo.bhn.BHNTransactionThreadVO;
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
public class BHNQueueActivationService extends UserAwareComponent {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private BHNActivationDAO activationDAO;

    @Autowired
    private BHNTransactionDAO transactionDAO;

    @Autowired
    private BHNRequestService requestService;

    @Autowired
    private ResourceDAO resourceDAO;

    public BHNTransactionThread initThread(String name) {
        BHNTransactionThread thread = new BHNTransactionThread(name);
        this.beanFactory.autowireBean(thread);
        return thread;
    }

    public List<BHNTransactionThreadVO> retrieveActivations() {
        final List<BHNTransactionThreadVO> result = new ArrayList<>();

        List<BHNActivation> activations = this.activationDAO.findByStatusAndQueueStatus(ACTIVATION_STATUS.ACTIVATION, ACTIVATION_QUEUE_STATUS.IDLE);
        activations.forEach(activation -> {
            BHNVoucher voucher = activation.getVoucher();
            BHNRequestContainer request = this.requestService.initRequest(voucher.getCardNumber(), voucher.getEan(), voucher.getAmount(),
                    activation.getShopCode(), activation.getTerminal(), activation.getType(), activation.getMerchant(), voucher.getProduct().getSupplier(),
                    voucher.getProduct().getPrimaryAccountNumber());

            BHNTransaction transaction = this.requestService.convert(request);

            transaction.setBhnActivation(activation);
            transaction.setCreatedAt(new Date());
            transaction.setQueueInsertDate(new Date());
            transaction.setDirection(TRANSACTION_DIRECTION.DISPATCH);
            this.transactionDAO.saveAndFlush(transaction, this.systemUserId());

            activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.QUEUED);
            this.activationDAO.saveAndFlush(activation, this.systemUserId());

            result.add(this.initVO(transaction, false));
        });

        return result;
    }

    public List<BHNTransactionThreadVO> retrieveReversals() {
        final List<BHNTransactionThreadVO> result = new ArrayList<>();

        List<BHNActivation> reversals = this.activationDAO.findByStatusAndQueueStatus(ACTIVATION_STATUS.REVERSAL, ACTIVATION_QUEUE_STATUS.IDLE);
        reversals.stream().filter(this::canSendReversal).forEach(activation -> {
            BHNTransaction originalTransaction = this.retrieveLastActivationTransaction(activation);
            if (Objects.nonNull(originalTransaction)) {
                BHNRequestContainer request = this.requestService.initReversal(originalTransaction);

                BHNTransaction transaction = this.requestService.convert(request);

                transaction.setBhnActivation(activation);
                transaction.setCreatedAt(new Date());
                transaction.setQueueInsertDate(new Date());
                transaction.setDirection(TRANSACTION_DIRECTION.DISPATCH);

                Resource reversalResource = this.retrieveReversalResource(originalTransaction.getResource());
                this.log("ID do recurso de cancelamento definido: " + reversalResource.getId() + " Servidor de cancelamento definido: " + reversalResource.getServer().getName());

                transaction.setResource(reversalResource);

                this.transactionDAO.saveAndFlush(transaction, this.systemUserId());

                activation.setQueueStatus(ACTIVATION_QUEUE_STATUS.QUEUED);
                this.activationDAO.saveAndFlush(activation, this.systemUserId());

                result.add(this.initVO(transaction, true));
            } else {
                this.log("Erro ao localizar a transação original da ativação de ID: " + activation.getId());
            }
        });

        return result;
    }

    public BHNTransactionThreadVO initVO(BHNTransaction transaction, boolean reversal) {
        BHNTransactionThreadVO vo = new BHNTransactionThreadVO(transaction.getId(), transaction, reversal);
        vo.addCallback(new BHNDefaultTransactionListener());

        BHNUpdateTransactionListener listener = new BHNUpdateTransactionListener();
        beanFactory.autowireBean(listener);
        vo.addCallback(listener);

        return vo;
    }

    private boolean canSendReversal(BHNActivation activation) {
        Date ultimoEnvio = activation.getReversalSentDate();
        boolean firstTime = Objects.isNull(ultimoEnvio);

        if (firstTime) {
            return true;
        } else {
            Long diferenca = this.millisecondsFromNow(ultimoEnvio);
            Long max = this.parameterDAO.get(ENVIRONMENT_PARAMETER.TEMPO_ENVIO_CANCELAMENTO);
            return diferenca > max;
        }
    }

    private Long millisecondsFromNow(Date ultimoEnvio) {
        Date dataAtual = new Date();
        return (dataAtual.getTime() - ultimoEnvio.getTime());
    }

    private BHNTransaction retrieveLastActivationTransaction(BHNActivation activation) {
        List<BHNTransaction> transactions = this.transactionDAO.findByBhnActivationAndDirectionOrderByCreatedAtDesc(activation, TRANSACTION_DIRECTION.DISPATCH );
        return transactions.stream().findFirst().orElse(null);
    }

    private Resource retrieveReversalResource(Resource recurso) {
        if (Objects.nonNull(recurso)) {
            this.log("buscarRecursoDeCancelamento: idRecursoOriginal: " + recurso.getId() + " Servidor original: " + recurso.getServer().getName());

            List<Resource> reversalResources = this.resourceDAO.findByServerAndType(recurso.getServer(), RESOURCE_TYPE.REVERSAL);
            if (Objects.nonNull(reversalResources)) {
                return reversalResources.stream().findFirst().orElse(null);
            } else {
                this.log("Erro buscando o Recurso de Cancelamento para o recurso : " + recurso.getId());
            }

        } else {
            this.log("Erro buscando o Recurso de Cancelamento !!!");
        }

        return null;
    }
}
