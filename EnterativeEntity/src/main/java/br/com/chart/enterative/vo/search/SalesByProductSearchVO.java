package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.vo.base.BaseVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author William Leite
 */
@Getter
@Setter
public class SalesByProductSearchVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    private AccountVO account;

    private Boolean removeEmpty;

    private SalesByProductSearchColumnVO[] columns;
}