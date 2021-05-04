package br.com.chart.enterative.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.event.BHNTransactionThreadEvent;
import java.util.Objects;
import br.com.chart.enterative.interfaces.BHNTransactionThreadListener;

public class BHNDefaultTransactionListener implements BHNTransactionThreadListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void notify(BHNTransactionThreadEvent event) {
        ACTIVATION_DECISION decisao = event.getDecisao();

        log.debug(event.getId() + " ID");

        if (Objects.nonNull(event.getResponseContainer())) {
            log.debug(event.getId() + " ResponseCode " + event.getResponseContainer().getResponse().getTransaction().getResponseCode());
        }

        log.debug(event.getId() + " Decisao " + decisao);
    }
}
