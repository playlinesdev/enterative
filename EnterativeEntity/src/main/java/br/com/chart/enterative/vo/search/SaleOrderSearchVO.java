package br.com.chart.enterative.vo.search;

import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.enums.SALE_ORDER_STATUS;
import br.com.chart.enterative.enums.SALE_ORDER_TYPE;
import br.com.chart.enterative.vo.base.NamedVO;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author William Leite
 */
@Getter
@Setter
public class SaleOrderSearchVO extends NamedVO {

    private static final long serialVersionUID = 1L;

    private AccountVO account;
    private ShopVO shop;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    private SALE_ORDER_STATUS status;
    private SALE_ORDER_TYPE type;
}