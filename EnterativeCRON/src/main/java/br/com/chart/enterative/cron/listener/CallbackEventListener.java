package br.com.chart.enterative.cron.listener;

import br.com.chart.enterative.dao.CallbackQueueDAO;
import br.com.chart.enterative.entity.CallbackQueue;
import br.com.chart.enterative.event.CallbackEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class CallbackEventListener implements ApplicationListener<CallbackEvent> {

    @Autowired
    private CallbackQueueDAO queueDAO;

    @Override
    public void onApplicationEvent(CallbackEvent event) {
        try {
            CallbackQueue callback = new CallbackQueue();
            callback.setBhnActivation(event.getBhnActivation());
            callback.setEpayActivation(event.getEpayActivation());
            queueDAO.saveAndFlush(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}