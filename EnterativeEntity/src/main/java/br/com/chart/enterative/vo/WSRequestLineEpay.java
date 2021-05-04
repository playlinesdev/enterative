package br.com.chart.enterative.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author William Leite
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WSRequestLineEpay implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String operator;
    private String areaCode;
    private String productId;
    private String phone;
    private BigDecimal amount;
}
