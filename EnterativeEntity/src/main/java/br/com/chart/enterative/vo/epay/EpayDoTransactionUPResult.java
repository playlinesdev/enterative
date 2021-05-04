package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("DoTransactionUPResult")
public class EpayDoTransactionUPResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("Amount")
    @Getter @Setter private String amount;
    
//    @XStreamAlias("Balance")
//    @Getter @Setter private ? balance;
    
//    @XStreamAlias("Card")
//    @Getter @Setter private ? card;
    
//    @XStreamAlias("Currency")
//    @Getter @Setter private ? currency;
    
//    @XStreamAlias("CustomData")
//    @Getter @Setter private ? customData;
    
    @XStreamAlias("HostTXID")
    @Getter @Setter private String hostTXID;
    
    @XStreamAlias("LocalDateTime")
    @Getter @Setter private String localDateTime;
    
    @XStreamAlias("MessageNumber")
    @Getter @Setter private String messageNumber;
    
//    @XStreamAlias("PinCredentials")
//    @Getter @Setter private ? pinCredentials;
    
    @XStreamAlias("ProductID")
    @Getter @Setter private String productId;
    
    @XStreamAlias("Receipt")
    @Getter @Setter private EpayReceipt receipt;
    
    @XStreamAlias("Result")
    @Getter @Setter private String result;
    
    @XStreamAlias("ResultText")
    @Getter @Setter private String resultText;
    
    @XStreamAlias("ServerDateTime")
    @Getter @Setter private String serverDateTime;
    
    @XStreamAlias("ServiceFee")
    @Getter @Setter private String serviceFee;
    
//    @XStreamAlias("ServiceTypeId")
//    @Getter @Setter private ? serviceTypeId;
    
//    @XStreamAlias("SubProductID")
//    @Getter @Setter private ? subProductId;
    
    @XStreamAlias("TerminalID")
    @Getter @Setter private String terminalId;
    
    @XStreamAlias("TransactionID")
    @Getter @Setter private String transactionId;
    
    @XStreamAlias("Type")
    @Getter @Setter private String type;
}