package br.com.chart.enterative.vo.search;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class AccountBalanceCheckSearchVO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private boolean removeEmpty;
}