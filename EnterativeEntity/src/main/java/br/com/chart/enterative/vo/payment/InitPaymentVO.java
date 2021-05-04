package br.com.chart.enterative.vo.payment;

import br.com.chart.enterative.enums.PAGSEGURO_PAYMENT_METHOD;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class InitPaymentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String orderPaymentToken;
    @Getter @Setter private String paymentMethod;
    @Getter @Setter private String redirectUrl;
    // Pagseguro
    @Getter @Setter private PAGSEGURO_PAYMENT_METHOD[] excludedMethods;
}
