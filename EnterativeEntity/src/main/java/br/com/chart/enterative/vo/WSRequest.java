package br.com.chart.enterative.vo;

import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
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
