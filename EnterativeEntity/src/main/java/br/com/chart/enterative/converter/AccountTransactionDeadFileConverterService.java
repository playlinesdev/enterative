package br.com.chart.enterative.converter;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopProductCommissionDAO;
import br.com.chart.enterative.entity.AccountTransactionDeadFile;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.vo.AccountTransactionCategoryVO;
import br.com.chart.enterative.entity.vo.AccountTransactionVO;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.PurchaseOrderLineVO;
import br.com.chart.enterative.entity.vo.SaleOrderLineVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.helper.EnterativeReflectionUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class AccountTransactionDeadFileConverterService extends ConverterService<AccountTransactionDeadFile, AccountTransactionVO> {

    @Autowired
    private ShopProductCommissionDAO shopProductCommissionDAO;

    public AccountTransactionDeadFileConverterService(EnterativeReflectionUtils reflectionUtils) {
        super(reflectionUtils);
    }

    @Override
    public AccountTransactionDeadFile convert(AccountTransactionVO vo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AccountTransactionVO convert(AccountTransactionDeadFile entity) {
        AccountTransactionVO vo = new AccountTransactionVO();

        try {
            Product product = null;
            vo.setAlteredAt(entity.getAlteredAt());
            vo.setAlteredBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getAlteredBy()));
            vo.setCreatedAt(entity.getCreatedAt());
            vo.setCreatedBy(this.reflectionUtils.asNamedLink(UserVO::new, entity.getCreatedBy()));
            vo.setAmount(entity.getAmount().setScale(2, RoundingMode.FLOOR));
            if (Objects.nonNull(entity.getCategory())) {
                vo.setCategory(this.reflectionUtils.asNamedLink(AccountTransactionCategoryVO::new, entity.getCategory()));
            }
            vo.setId(entity.getId());
            if (Objects.nonNull(entity.getSaleOrderLine())) {
                vo.setSaleOrderLine(this.reflectionUtils.asHollowLink(SaleOrderLineVO::new, entity.getSaleOrderLine()));
                vo.setSaleOrderNumber(entity.getSaleOrderLine().getSaleOrder().getId());

                product = entity.getSaleOrderLine().getProduct();
                if (Objects.nonNull(product)) {
                    vo.setProductId(product.getId());
                    vo.setProductName(product.getName());
                    Shop shop = entity.getSaleOrderLine().getSaleOrder().getShop();
                    if (Objects.nonNull(shop)) {
                        ShopProductCommission commission = this.shopProductCommissionDAO.findByShopAndProduct(shop, product);
                        if (Objects.nonNull(commission)) {
                            if (Objects.nonNull(commission.getAmount()) && commission.getAmount().signum() > 0) {
                                vo.setCommissionAmount(commission.getAmount());
                            } else if (Objects.nonNull(commission.getPercentage()) && commission.getPercentage().signum() > 0) {
                                vo.setCommissionPercentage(commission.getPercentage().multiply(new BigDecimal(100)));
                            }
                        } else {
                            vo.setCommissionAmount(BigDecimal.ZERO);
                            vo.setCommissionPercentage(BigDecimal.ZERO);
                        }
                    }
                }
            }
            if (Objects.nonNull(entity.getPurchaseOrderLine())) {
                vo.setPurchaseOrderLine(this.reflectionUtils.asHollowLink(PurchaseOrderLineVO::new, entity.getPurchaseOrderLine()));
                vo.setPurchaseOrderNumber(entity.getPurchaseOrderLine().getPurchaseOrder().getId());
                if (Objects.isNull(product)) {
                    vo.setProductName(entity.getPurchaseOrderLine().getName());
                }
            }
            if (Objects.nonNull(entity.getAccount())) {
                vo.setAccount(this.reflectionUtils.asNamedLink(AccountVO::new, entity.getAccount()));
            }
            vo.setStatus(entity.getStatus());
            vo.setTransactionDate(entity.getTransactionDate());
            vo.setType(entity.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }
}
