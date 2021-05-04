package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.SaleOrderVO;
import br.com.chart.enterative.entity.vo.ShopVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SaleOrderConverterService extends ConverterService<SaleOrder, SaleOrderVO> {

    @Autowired
    private SaleOrderLineConverterService saleOrderLineConverterService;

    public SaleOrderConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public SaleOrder convert(SaleOrderVO vo) {
        SaleOrder entity = new SaleOrder();
        entity.setAccount(this.reflectionUtils.asHollowLink(Account::new, vo.getAccount()));
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setAmount(vo.getAmount());
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setCustomerMobile(vo.getCustomerMobile());
        entity.setEmailStatus(vo.getEmailStatus());
        entity.setId(vo.getId());
        entity.setLines(vo.getLines().stream().map(l -> {
            SaleOrderLine line = this.saleOrderLineConverterService.convert(l);
            line.setSaleOrder(entity);
            return line;
        }).collect(Collectors.toList()));
        entity.setShop(this.reflectionUtils.asHollowLink(Shop::new, vo.getShop()));
        entity.setStatus(vo.getStatus());
        entity.setType(vo.getType());
        return entity;
    }

    @Override
    public SaleOrderVO convert(SaleOrder entity) {
        SaleOrderVO vo = new SaleOrderVO();
        vo.setAccount(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getAccount()));
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getAlteredBy()));
        vo.setAmount(entity.getAmount());
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asHollowLink(UserVO::new, entity.getCreatedBy()));
        vo.setCustomerMobile(entity.getCustomerMobile());
        vo.setEmailStatus(entity.getEmailStatus());
        vo.setId(entity.getId());
        vo.setLines(entity.getLines().stream().map(this.saleOrderLineConverterService::convert).collect(Collectors.toList()));
        vo.setShop(this.reflectionUtils.asNamedLink(ShopVO::new, entity.getShop()));
        vo.setStatus(entity.getStatus());
        vo.setType(entity.getType());
        return vo;
    }

}
