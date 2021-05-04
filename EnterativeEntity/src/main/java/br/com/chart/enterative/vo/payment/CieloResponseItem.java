package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class CieloResponseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String $id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private Integer unitPrice;
    @Getter @Setter private Integer quantity;
    @Getter @Setter private String type;
    @Getter @Setter private String sku;
    @Getter @Setter private Integer weight;
}
