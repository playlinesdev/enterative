package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("GetSalesReportResponse")
public class EpayGetSalesReportResponse {

    @XStreamAlias("GetSalesReportResult")
    @Getter @Setter private EpayGetSalesReportResult result;
}
