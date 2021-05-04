package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ResourceSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;
    @Getter @Setter private ServerVO server;
}
