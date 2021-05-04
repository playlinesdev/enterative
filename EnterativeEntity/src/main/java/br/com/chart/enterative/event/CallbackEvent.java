package br.com.chart.enterative.event;

import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.EpayActivation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Evento para inserção de Callback na Queue.
 *
 * @author William Leite
 */
public class CallbackEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    @Getter private final BHNActivation bhnActivation;
    @Getter private final EpayActivation epayActivation;

    public CallbackEvent(Object source, BHNActivation bhnActivation) {
        super(source);
        this.bhnActivation = bhnActivation;
        this.epayActivation = null;
    }
    
    public CallbackEvent(Object source, EpayActivation epayActivation) {
        super(source);
        this.bhnActivation = null;
        this.epayActivation = epayActivation;
    }
}
