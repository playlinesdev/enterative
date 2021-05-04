package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SequencialVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String retrievalReferenceNumber;
    @Getter @Setter private String systemTraceAuditNumber;

    public SequencialVO(String retrievealReferenceNumber, String systemTraceAuditNumber) {
        this.retrievalReferenceNumber = retrievealReferenceNumber;
        this.systemTraceAuditNumber = systemTraceAuditNumber;
    }
}
