package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author William Leite
 */
public class SDFValidationSearchVO extends NamedVO{

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long id;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Getter @Setter private Date createdAt;
    @Getter @Setter private SDF_FILE_STATUS status;
}
