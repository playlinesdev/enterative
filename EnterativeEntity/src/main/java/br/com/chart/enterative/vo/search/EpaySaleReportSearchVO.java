package br.com.chart.enterative.vo.search;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Producao
 */
public class EpaySaleReportSearchVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date startDate;
}