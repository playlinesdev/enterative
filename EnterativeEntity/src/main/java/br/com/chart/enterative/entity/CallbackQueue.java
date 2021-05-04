package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity()
@Table(name = "callback_queue")
public class CallbackQueue extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter protected Long id;
    
    @ManyToOne
    @JoinColumn(name = "bhn_activation_id")
    @Getter @Setter private BHNActivation bhnActivation;
    
    @ManyToOne
    @JoinColumn(name = "epay_activation_id")
    @Getter @Setter private EpayActivation epayActivation;
}