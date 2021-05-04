package br.com.chart.enterative.cron.callback;

import br.com.chart.enterative.entity.CallbackQueue;
import br.com.chart.enterative.cron.CallbackThread;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.service.CallbackQueueService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Controla o callback das ativações
 *
 * @author William Leite
 */
@Component
public class CallbackController extends UserAwareComponent {

    private final CallbackQueueService callbackService;

    private final LinkedHashSet<CallbackQueue> queue;
    private final Queue<CallbackThread> threadQueue;
    private final Queue<CallbackThread> executionList;

    public CallbackController(EnvParameterDAO parameterDAO, CallbackQueueService callbackService) {
        int maxThreads = parameterDAO.get(ENVIRONMENT_PARAMETER.THREADS_CALLBACK);
        this.queue = new LinkedHashSet<>();
        this.threadQueue = new LinkedList<>();
        this.executionList = new LinkedList<>();

        this.callbackService = callbackService;

        while (this.threadQueue.size() < maxThreads) {
            this.threadQueue.add(this.callbackService.initThread());
        }
    }

    @Scheduled(fixedDelay = 500)
    public void scheduledQueueManager() {
        if (!this.queue.isEmpty() && !this.threadQueue.isEmpty()) {
            CallbackThread thread = this.threadQueue.poll();

            Iterator<CallbackQueue> iterator = this.queue.iterator();
            CallbackQueue callback = iterator.next();
            iterator.remove();

            thread.setQueue(callback);
            thread.start();

            this.executionList.add(thread);
        }

        if (!this.executionList.isEmpty()) {
            CallbackThread processador = this.executionList.peek();
            if (!processador.isAlive()) {
                this.executionList.poll();
                this.threadQueue.add(this.callbackService.initThread());
            }
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void scheduledQueueInsert() {
        this.queue.addAll(this.callbackService.retrieveQueue());
    }
}
