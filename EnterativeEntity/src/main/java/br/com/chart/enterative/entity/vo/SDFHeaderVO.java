package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFHeaderVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Date fileTransmissionDate;
    @Getter @Setter private Date fileReportingDate;
    @Getter @Setter private String partnerId;
    @Getter @Setter private String partnerName;
    @Getter @Setter private String filler;
}
