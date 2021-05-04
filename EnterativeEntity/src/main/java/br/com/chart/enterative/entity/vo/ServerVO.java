package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.STATUS;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ServerVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private STATUS status;
    @Getter @Setter private ProductVO echoProduct;
    @Getter @Setter private Integer sequence;
    @Getter @Setter private ACTIVATION_PROCESS activationProcess;
}
