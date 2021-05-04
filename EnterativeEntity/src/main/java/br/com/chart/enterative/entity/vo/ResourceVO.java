package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;

import br.com.chart.enterative.enums.RESOURCE_TYPE;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ResourceVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String name;
    @Getter @Setter private ServerVO server;
    @Getter @Setter private RESOURCE_TYPE type;
    @Getter @Setter private String url;
}
