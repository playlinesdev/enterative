package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class AccountTransactionSearchGroupingVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Long id;
    @Getter @Setter private String name;
    @Getter @Setter private boolean selected;

    public AccountTransactionSearchGroupingVO() {

    }

    public AccountTransactionSearchGroupingVO(Long id, String name) {
        this.id = id;
        this.name = name;
        this.selected = false;
    }
}
