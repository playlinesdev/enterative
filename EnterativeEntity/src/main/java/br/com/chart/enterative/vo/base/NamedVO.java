package br.com.chart.enterative.vo.base;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class NamedVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter protected String name;
}
