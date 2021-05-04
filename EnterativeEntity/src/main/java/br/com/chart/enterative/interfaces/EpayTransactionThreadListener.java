package br.com.chart.enterative.interfaces;

import br.com.chart.enterative.event.EpayTransactionThreadEvent;

public interface EpayTransactionThreadListener {

    public void notify(EpayTransactionThreadEvent event);

}
