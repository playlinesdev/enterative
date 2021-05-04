package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SMS_STATUS;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author William Leite
 */
@Getter
@Setter
public class SaleOrderLineVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    // Flutter
    private String translatedStatus;
    private String translatedActivationStatus;
    private String translatedResponse;
    private String translatedResponseAux;

    private ProductVO product;
    private Date returnDate;
    private SALE_ORDER_LINE_STATUS status;
    private String externalCode;
    private RESPONSE_CODE response;
    private RESPONSE_CODE responseAux;
    private ACTIVATION_STATUS activationStatus;
    private CALLBACK_STATUS callbackStatus;
    private String barcode;
    private BigDecimal amount;

    private EMAIL_STATUS userEmailStatus;
    private String userEmail;

    private SMS_STATUS smsStatus;
    private String userCellphone;

    private String areaCode;
    private String catalogId;
    private String operator;
    private String phone;
    private String pin;
}
