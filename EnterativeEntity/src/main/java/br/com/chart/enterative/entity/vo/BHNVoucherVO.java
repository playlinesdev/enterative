package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class BHNVoucherVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String ean;
    @Getter @Setter private String cardNumber;
    @Getter @Setter private String amount;
    @Getter @Setter private String pin;
    @Getter @Setter private ProductVO product;
}
