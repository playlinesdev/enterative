package br.com.chart.enterative.vo.epay;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author user
 */ 
public class EpayRequestContainer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private String xmlBody;
    @Getter @Setter private String soapAction;
    @Getter @Setter private String action;
}
