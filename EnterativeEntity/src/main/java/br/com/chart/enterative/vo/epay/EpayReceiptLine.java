package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("a:ResponseReceiptLine")
public class EpayReceiptLine implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("a:Value")
    @Getter @Setter private String value;

}
