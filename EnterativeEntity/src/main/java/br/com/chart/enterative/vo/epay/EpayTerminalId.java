package br.com.chart.enterative.vo.epay;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class EpayTerminalId implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Getter @Setter private ? retailerAcc;
//    @Getter @Setter private ? shopId;
    @Getter @Setter private String value;
}
