package br.com.chart.enterative.vo;

import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @author William Leite
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WSRequestLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String externalCode;
    private String barcode;
    private WSRequestLineEpay epay;
    
    private RESPONSE_CODE response;
    private RESPONSE_CODE responseAux;
    private ACTIVATION_STATUS activationStatus;
    private ACTIVATION_PROCESS activationProcess;

}
