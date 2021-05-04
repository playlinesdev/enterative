package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.PAGSEGURO_PAYMENT_METHOD;
import br.com.chart.enterative.enums.STATUS;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class PagseguroPaymentMethodVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private PAGSEGURO_PAYMENT_METHOD paymentMethod;
    @Getter @Setter private STATUS status;
}
