package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.STATUS;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "server")
public class Server extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private STATUS status;

    @ManyToOne
    @JoinColumn(name = "echo_product_id")
    @Getter @Setter private Product echoProduct;

    @Column(name = "server_sequence")
    @Getter @Setter private Integer sequence;
    
    @Enumerated
    @Column(name = "activation_process")
    @Getter @Setter private ACTIVATION_PROCESS activationProcess;
}
