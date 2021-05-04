package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.PurchaseOrderLineVO;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
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
public class PurchaseOrderConverterService extends ConverterService<PurchaseOrder, PurchaseOrderVO> {

    @Autowired
    private PurchaseOrderLineConverterService purchaseOrderLineConverterService;

    public PurchaseOrderConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public PurchaseOrder convert(PurchaseOrderVO vo) {
        PurchaseOrder entity = new PurchaseOrder();
        entity.setAccount(this.reflectionUtils.asHollowLink(Account::new, vo.getAccount()));
        entity.setActivatedDate(vo.getActivatedDate());
        entity.setActivatedUser(this.reflectionUtils.asHollowLink(User::new, vo.getActivatedUser()));
        entity.setAlteredAt(vo.getAlteredAt());
        entity.setAlteredBy(this.reflectionUtils.asHollowLink(User::new, vo.getAlteredBy()));
        entity.setCreatedAt(vo.getCreatedAt());
        entity.setCreatedBy(this.reflectionUtils.asHollowLink(User::new, vo.getCreatedBy()));
        entity.setId(vo.getId());
        entity.setLines(vo.getLines().stream().map(l -> {
            PurchaseOrderLine line = this.purchaseOrderLineConverterService.convert(l);
            line.setPurchaseOrder(entity);
            line.setStatus(vo.getStatus());
            return line;
        }).collect(Collectors.toList()));
        entity.setShop(this.reflectionUtils.asHollowLink(Shop::new, vo.getShop()));
        entity.setStatus(vo.getStatus());
        entity.setTotalAmount(vo.getTotalAmount());
        return entity;
    }

    @Override
    public PurchaseOrderVO convert(PurchaseOrder entity) {
        PurchaseOrderVO vo = new PurchaseOrderVO();
        vo.setAccount(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getAccount()));
        vo.setActivatedDate(entity.getActivatedDate());
        vo.setActivatedUser(this.reflectionUtils.asNamedLink(UserVO::new, entity.getActivatedUser()));
        vo.setAlteredAt(entity.getAlteredAt());
        vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
        vo.setCreatedAt(entity.getCreatedAt());
        vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
        vo.setId(entity.getId());
        vo.setLines(entity.getLines().stream().map(l -> {
            PurchaseOrderLineVO line = this.purchaseOrderLineConverterService.convert(l);
            line.setStatus(l.getStatus());
            return line;
        }).collect(Collectors.toList()));
        vo.setShop(this.reflectionUtils.asNamedLink(ShopVO::new, entity.getShop()));
        vo.setStatus(entity.getStatus());
        vo.setTotalAmount(entity.getTotalAmount());
        return vo;
    }
}
