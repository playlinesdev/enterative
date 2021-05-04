package br.com.chart.enterative.entity;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode(callSuper = false)
@ToString
public class CallbackQueueID implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "bhn_activation_id")
    @Getter @Setter private BHNActivation bhnActivation;
    
    @ManyToOne
    @JoinColumn(name = "epay_activation_id")
    @Getter @Setter private EpayActivation epayActivation;

    public CallbackQueueID() {
    }

    public CallbackQueueID(BHNActivation bhnActivation, EpayActivation epayActivation) {
        this.bhnActivation = bhnActivation;
        this.epayActivation = epayActivation;
    }
}
