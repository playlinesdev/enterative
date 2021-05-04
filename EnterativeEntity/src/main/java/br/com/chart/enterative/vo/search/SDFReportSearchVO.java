package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @author William Leite
 */
public class SDFReportSearchVO extends NamedVO{

    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date start;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date end;
}
