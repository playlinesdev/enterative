package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class EpayDisplayCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("a:DisplayGroup")
    @Getter @Setter private String displayGroup;

    @XStreamAlias("a:ProductType")
    @Getter @Setter private String productType;

    @Override
    public String toString() {
        return String.format("%s|%s", this.displayGroup, this.productType);
    }
}
