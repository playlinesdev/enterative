package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.base.NamedVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

/**
 *
 * @author William Leite
 */
public class SortColumnVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String columnName;
    @Getter @Setter private Direction direction;

    public SortColumnVO() {
    }

    public SortColumnVO(Long id, String columnName, String name, Direction direction) {
        this.id = id;
        this.name = name;
        this.columnName = columnName;
        this.direction = direction;
    }
}
