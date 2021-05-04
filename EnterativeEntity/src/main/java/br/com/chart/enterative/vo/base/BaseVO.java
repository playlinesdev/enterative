package br.com.chart.enterative.vo.base;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@SuppressWarnings("serial")
public abstract class BaseVO implements Serializable {

    @Getter @Setter protected Long id;
}
