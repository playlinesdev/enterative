package br.com.enterative.demo.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.enterative.demo.enums.ACTIVATION_TYPE;
import br.com.enterative.demo.enums.RESPONSE_CODE;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WSRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String merchant;
    private String shop;
    private String terminal;
    private ACTIVATION_TYPE activationType;
    private String callbackURL;
    private RESPONSE_CODE response;
    private List<WSRequestLine> lines;


}
