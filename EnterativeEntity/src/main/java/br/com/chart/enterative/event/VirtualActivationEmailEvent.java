package br.com.chart.enterative.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author William Leite
 */
public class VirtualActivationEmailEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    @Getter private final Long saleOrderID;

    public VirtualActivationEmailEvent(Object source, Long saleOrderID) {
        super(source);
        this.saleOrderID = saleOrderID;
    }
}
