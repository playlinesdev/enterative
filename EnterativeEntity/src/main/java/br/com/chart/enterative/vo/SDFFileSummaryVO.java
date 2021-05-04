package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.BaseVO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class SDFFileSummaryVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private List<SDFFileSummaryDetailVO> details;
    @Getter @Setter private List<SDFFileSummaryTransactionVO> transactions;
    @Getter @Setter private SDFFileSummaryDiffVO difference;
    @Getter @Setter private boolean different;
}
