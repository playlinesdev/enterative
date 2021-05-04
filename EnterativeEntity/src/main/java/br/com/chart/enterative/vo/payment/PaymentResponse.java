package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PaymentResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String responseCode;
    @Getter @Setter private String message;
    @Getter @Setter private Object response;
    @Getter @Setter private String responseType;
}
