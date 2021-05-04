package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.vo.ResourceVO;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class BHNTransactionSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    
    @Getter @Setter private TRANSACTION_DIRECTION direction;

    @Getter @Setter private ResourceVO resource;

    @Getter @Setter private String primaryAccountNumber;

    @Getter @Setter private String productId;

    @Getter @Setter private String redemptionPin;

    @Getter @Setter private String systemTraceAuditNumber;
}
