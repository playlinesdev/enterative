package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("Report")
public class EpayReport {

    @XStreamAlias("Status")
    @Getter @Setter private Integer status;

    @XStreamAlias("Mensagem")
    @Getter @Setter private String mensagem;

    @XStreamImplicit
    @XStreamAlias("Item")
    @Getter @Setter private EpayReportItem[] items;
}