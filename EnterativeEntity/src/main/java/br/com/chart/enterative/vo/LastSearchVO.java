package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.NamedVO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 * @param <S>
 */
public class LastSearchVO<S extends NamedVO> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String listName;
    @Getter @Setter private S searchForm;
    @Getter @Setter private LastSearchPageable pageable;
}
