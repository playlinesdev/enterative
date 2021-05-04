package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACTIVATION_QUEUE_STATUS;


import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import lombok.Getter;
import lombok.Setter;

public class EpayActivationVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ACTIVATION_STATUS status;
    @Getter @Setter private ACTIVATION_QUEUE_STATUS queueStatus;
    @Getter @Setter private CALLBACK_STATUS callbackStatus;
    @Getter @Setter private String responseCode;
    @Getter @Setter private Integer ttlActivation;
    @Getter @Setter private MerchantVO merchant;
    @Getter @Setter private EpayVoucherVO voucher;
    @Getter @Setter private String shopCode;
    @Getter @Setter private String terminal;
    @Getter @Setter private String externalCode;
    @Getter @Setter private String callbackurl;
}