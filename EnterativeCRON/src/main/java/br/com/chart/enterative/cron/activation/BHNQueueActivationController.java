package br.com.chart.enterative.cron.activation;

import br.com.chart.BootstrapApplication;
import br.com.chart.enterative.cron.BHNTransactionThread;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.service.activation.BHNQueueActivationService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.ActiveResourceVO;
import br.com.chart.enterative.vo.bhn.BHNTransactionThreadVO;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Responsavel por administrar as filas de Ativacao e Desfazimento. As ativacoes vem do banco ordenadas. Os desfazimentos sao ativacoes que nao deram certo.
 *
 * @author Cristhiano Roberto
 *
 */
@Component
public class BHNQueueActivationController extends UserAwareComponent {

    private final Queue<BHNTransactionThreadVO> queue;
    private final Queue<BHNTransactionThread> activationThreadQueue;
    private final Queue<BHNTransactionThread> reversalThreadQueue;
    private final Queue<BHNTransactionThread> executionList;

    private final BHNQueueActivationService queueActivationService;

    public BHNQueueActivationController(EnvParameterDAO parameterDAO, BHNQueueActivationService queueActivationService) {
        this.queueActivationService = queueActivationService;

        queue = new LinkedList<>();
        activationThreadQueue = new LinkedList<>();
        reversalThreadQueue = new LinkedList<>();
        executionList = new LinkedList<>();

        int maxActivationThreads = parameterDAO.get(ENVIRONMENT_PARAMETER.THREADS_ACTIVATION);
        int maxReversalThreads = parameterDAO.get(ENVIRONMENT_PARAMETER.THREADS_REVERSAL);

        while (this.activationThreadQueue.size() < maxActivationThreads) {
            this.activationThreadQueue.add(this.queueActivationService.initThread("Transaction Thread"));
        }

        while (this.reversalThreadQueue.size() < maxReversalThreads) {
            this.reversalThreadQueue.add(this.queueActivationService.initThread("Reversal Thread"));
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduledQueueManager() {
        try {
            if (BootstrapApplication.isActive(ACTIVATION_PROCESS.BHN) && !queue.isEmpty()) {
                BHNTransactionThread thread = null;
                BHNTransactionThreadVO threadVO = queue.peek();

                //Remove a thread da lista
                if (threadVO.isReversal() && !reversalThreadQueue.isEmpty()) {
                    thread = reversalThreadQueue.poll();
                } else if (!threadVO.isReversal() && !activationThreadQueue.isEmpty()) {
                    thread = activationThreadQueue.poll();
                }

                if (Objects.nonNull(thread)) {
                    queue.poll();
                    executionList.add(thread);

                    thread.setThreadVO(threadVO);
                    
                    ActiveResourceVO activeResource = BootstrapApplication.activeResources.get(ACTIVATION_PROCESS.BHN);
                    thread.setActiveResource(activeResource.getResource());
                    thread.start();
                }
            }

            //Retorna processador para a fila
            if (!executionList.isEmpty()) {
                //precisa verificar se a thread ja acabou para adicionar uma nova na lista
                BHNTransactionThread thread = executionList.peek();
                if (!thread.isAlive()) {
                    if (!thread.isReversal()) {
                        this.activationThreadQueue.add(this.queueActivationService.initThread("Transaction Thread"));
                    } else {
                        this.reversalThreadQueue.add(this.queueActivationService.initThread("Reversal Thread"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 1000) // busca ativação a cada 1 segundo
    public void retrieveActivations() {
        if (BootstrapApplication.isActive(ACTIVATION_PROCESS.BHN)) {
            this.queue.addAll(this.queueActivationService.retrieveActivations());
        }
    }

    //@Scheduled(fixedRate = 15000)
    @Scheduled(fixedRate = 1000 ) // busca desfazimento a cada 15 minutos
    //@Scheduled(fixedRate = 30 *1000)
    //@Scheduled(fixedDelay = 1000) // 1 Segundo para homologação
    public void retrieveReversals() {
        if (BootstrapApplication.isActive(ACTIVATION_PROCESS.BHN)) {
            this.queue.addAll(this.queueActivationService.retrieveReversals());
        }
    }

}
