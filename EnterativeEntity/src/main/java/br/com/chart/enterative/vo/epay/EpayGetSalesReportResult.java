package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("GetSalesReportResult")
public class EpayGetSalesReportResult {

    @XStreamAlias("Report")
    @Getter @Setter private EpayReport report;
}
