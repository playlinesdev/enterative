package br.com.chart.enterative.vo;

import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * VO com os dados a serem enviados durante o callback ao cliente
 *
 * @author William Leite
 */
public class CallbackDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String externalCode;
    @Getter @Setter private ACTIVATION_STATUS activationStatus;
    @Getter @Setter private String responseCode;
    @Getter @Setter private Date lastUpdate;
}
