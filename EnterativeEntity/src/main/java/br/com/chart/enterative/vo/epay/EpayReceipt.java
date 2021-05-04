package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("Receipt")
public class EpayReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("a:Customer")
    @Getter @Setter private EpayReceiptLinesContainer customer;
    
    @XStreamAlias("a:Merchant")
    @Getter @Setter private EpayReceiptLinesContainer merchant;
}
