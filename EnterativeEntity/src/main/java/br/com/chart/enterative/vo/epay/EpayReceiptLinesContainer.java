package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class EpayReceiptLinesContainer implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @XStreamAlias("a:Lines")
    @Getter @Setter private List<EpayReceiptLine> lines;
}