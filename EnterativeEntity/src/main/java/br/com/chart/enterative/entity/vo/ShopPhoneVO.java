package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.SHOP_PHONE_TYPE;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ShopPhoneVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String phone;
    @Getter @Setter private SHOP_PHONE_TYPE type;
    @Getter @Setter private String contact;
}
