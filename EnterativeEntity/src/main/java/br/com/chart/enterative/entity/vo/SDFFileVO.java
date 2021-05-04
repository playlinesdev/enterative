package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.SDF_FILE_STATUS;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFFileVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String name;
    @Getter @Setter private SDFHeaderVO header;
    @Getter @Setter private List<SDFDetailVO> details;
    @Getter @Setter private SDFTrailerVO trailer;
    @Getter @Setter private SDF_FILE_STATUS status;
}
