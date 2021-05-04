package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class CieloResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String $id;
    @Getter @Setter private String merchantId;
    @Getter @Setter private String orderNumber;
    @Getter @Setter private String softDescriptor;
    @Getter @Setter private CieloResponseCart cart;
    @Getter @Setter private CieloResponseShipping shipping;
    @Getter @Setter private CieloResponseOptions options;
    @Getter @Setter private CieloResponseSettings settings;
}
