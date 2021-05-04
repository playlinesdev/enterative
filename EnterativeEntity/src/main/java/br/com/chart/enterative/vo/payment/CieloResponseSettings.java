package br.com.chart.enterative.vo.payment;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class CieloResponseSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String $id;
    @Getter @Setter private String checkoutUrl;
    @Getter @Setter private String profile;
    @Getter @Setter private String version;
    @Getter @Setter private String integrationType;
}
