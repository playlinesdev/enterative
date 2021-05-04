package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author William Leite
 */
public class BHNActivationSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String externalCode;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date endDate;
}
