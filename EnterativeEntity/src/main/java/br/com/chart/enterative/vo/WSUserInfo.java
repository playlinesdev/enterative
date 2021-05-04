package br.com.chart.enterative.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class WSUserInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String login;
    private String password;
    private Long shop;
    private  Long account;
    private BigDecimal threshold;

    public WSUserInfo(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
