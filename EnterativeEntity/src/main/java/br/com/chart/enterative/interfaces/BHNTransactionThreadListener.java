package br.com.chart.enterative.interfaces;

import br.com.chart.enterative.event.BHNTransactionThreadEvent;

public interface BHNTransactionThreadListener {

    public void notify(BHNTransactionThreadEvent event);

}
