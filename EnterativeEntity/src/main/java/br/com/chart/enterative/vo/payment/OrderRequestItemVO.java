package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class OrderRequestItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Integer sequence;
    @Getter @Setter private Long productId;
    @Getter @Setter private String productName;
    @Getter @Setter private BigDecimal quantity;
    @Getter @Setter private BigDecimal unitPrice;
}
