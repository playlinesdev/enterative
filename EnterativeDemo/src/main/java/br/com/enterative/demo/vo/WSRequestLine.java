package br.com.enterative.demo.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.enterative.demo.enums.ACTIVATION_STATUS;
import br.com.enterative.demo.enums.RESPONSE_CODE;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WSRequestLine implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String externalCode;
    private String barcode;
    private RESPONSE_CODE response;
    private RESPONSE_CODE responseAux;
    private ACTIVATION_STATUS activationStatus;
	
}
