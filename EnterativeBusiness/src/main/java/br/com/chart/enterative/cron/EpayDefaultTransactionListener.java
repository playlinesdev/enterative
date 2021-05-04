package br.com.chart.enterative.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.chart.enterative.enums.ACTIVATION_DECISION;
import br.com.chart.enterative.event.EpayTransactionThreadEvent;
import java.util.Objects;
import br.com.chart.enterative.interfaces.EpayTransactionThreadListener;

public class EpayDefaultTransactionListener implements EpayTransactionThreadListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void notify(EpayTransactionThreadEvent event) {
        ACTIVATION_DECISION decisao = event.getDecisao();

        log.debug(event.getId() + " ID");

        if (Objects.nonNull(event.getResponseContainer())) {
            log.debug(event.getId() + " Result " + event.getResponseContainer().getResult());
        }

        log.debug(event.getId() + " Decisao " + decisao);
    }
}
