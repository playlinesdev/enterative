package br.com.chart.enterative.cron.activation;

import br.com.chart.BootstrapApplication;
import br.com.chart.enterative.cron.EpayTransactionThread;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.service.activation.EpayQueueActivationService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.ActiveResourceVO;
import br.com.chart.enterative.vo.epay.EpayTransactionThreadVO;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EpayQueueActivationController extends UserAwareComponent {

    private final Queue<EpayTransactionThreadVO> queue;
    private final Queue<EpayTransactionThread> activationThreadQueue;
    private final Queue<EpayTransactionThread> executionList;

    private final EpayQueueActivationService queueActivationService;

    public EpayQueueActivationController(EnvParameterDAO parameterDAO, EpayQueueActivationService queueActivationService) {
        this.queueActivationService = queueActivationService;

        queue = new LinkedList<>();
        activationThreadQueue = new LinkedList<>();
        executionList = new LinkedList<>();

        int maxActivationThreads = parameterDAO.get(ENVIRONMENT_PARAMETER.THREADS_ACTIVATION);

        while (this.activationThreadQueue.size() < maxActivationThreads) {
            this.activationThreadQueue.add(this.queueActivationService.initThread("Transaction Thread"));
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduledQueueManager() {
        try {
            if (BootstrapApplication.isActive(ACTIVATION_PROCESS.EPAY) && !queue.isEmpty()) {
                EpayTransactionThread thread = null;
                EpayTransactionThreadVO threadVO = queue.peek();

                //Remove a thread da lista
                if (!activationThreadQueue.isEmpty()) {
                    thread = activationThreadQueue.poll();
                }

                if (Objects.nonNull(thread)) {
                    queue.poll();
                    executionList.add(thread);

                    thread.setThreadVO(threadVO);
                    
                    ActiveResourceVO activeResource = BootstrapApplication.activeResources.get(ACTIVATION_PROCESS.EPAY);
                    thread.setActiveResource(activeResource.getResource());
                    thread.start();
                }
            }

            //Retorna processador para a fila
            if (!executionList.isEmpty()) {
                //precisa verificar se a thread ja acabou para adicionar uma nova na lista
                EpayTransactionThread thread = executionList.peek();
                if (!thread.isAlive()) {
                    this.activationThreadQueue.add(this.queueActivationService.initThread("Transaction Thread"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 1000) // busca ativação a cada 1 segundo
    public void retrieveActivations() {
        if (BootstrapApplication.isActive(ACTIVATION_PROCESS.EPAY)) {
            this.queue.addAll(this.queueActivationService.retrieveActivations());
        }
    }
}