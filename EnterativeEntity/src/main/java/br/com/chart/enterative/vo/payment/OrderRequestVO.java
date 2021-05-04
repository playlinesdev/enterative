package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class OrderRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String partnerToken;
    @Getter @Setter private OrderRequestItemVO[] items;
}
