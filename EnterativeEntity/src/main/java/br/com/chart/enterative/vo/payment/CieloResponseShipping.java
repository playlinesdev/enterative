package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class CieloResponseShipping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String $id;
    @Getter @Setter private String type;
}
