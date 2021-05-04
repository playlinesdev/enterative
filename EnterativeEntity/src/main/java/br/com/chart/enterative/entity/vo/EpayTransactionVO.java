package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.*;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class EpayTransactionVO extends UserAwareEntity {

    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private Date queueInsertDate;
    @Getter @Setter private Date transactionReturnDate;
    @Getter @Setter private TRANSACTION_DIRECTION direction;
    @Getter @Setter private ResourceVO resource;
    @Getter @Setter private EpayActivationVO activation;
    @Getter @Setter private String amount;
    @Getter @Setter private String localDateTime;
    @Getter @Setter private String messageNumber;
    @Getter @Setter private String phone;
    @Getter @Setter private String productId;
    @Getter @Setter private String requestType;
    @Getter @Setter private String sequence;
    @Getter @Setter private String serviceFee;
    @Getter @Setter private String terminalId;
    @Getter @Setter private String transactionId;
    @Getter @Setter private String result;
    @Getter @Setter private String resultText;
    @Getter @Setter private String hostTXID;
    @Getter @Setter private String receiptCustomer;
    @Getter @Setter private String receiptMerchant;
    @Getter @Setter private String serverDateTime;
}