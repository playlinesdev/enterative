package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
public class CallbackQueueVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BHNActivationVO bhnActivation;
    @Getter @Setter private EpayActivationVO epayActivation;
}
