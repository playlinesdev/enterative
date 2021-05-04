package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShopSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String code;
    @Getter @Setter private STATUS status;
}
