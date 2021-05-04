package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.PAGSEGURO_PAYMENT_METHOD;
import br.com.chart.enterative.enums.STATUS;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "pagseguro_payment_method")
public class PagseguroPaymentMethod extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "payment_method")
    @Enumerated
    @Getter @Setter private PAGSEGURO_PAYMENT_METHOD paymentMethod;

    @Column(name = "status")
    @Enumerated
    @Getter @Setter private STATUS status;
}
