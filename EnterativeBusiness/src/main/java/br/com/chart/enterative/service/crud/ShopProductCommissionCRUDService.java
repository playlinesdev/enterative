package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopProductCommissionDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.Product;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.ShopProductCommission;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.entity.vo.ShopProductCommissionVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShopProductCommissionSearchVO;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShopProductCommissionCRUDService extends UserAwareCRUDService<ShopProductCommission, Long, ShopProductCommissionVO, ShopProductCommissionSearchVO> {

    @Autowired
    private PurchaseOrderCRUDService purchaseOrderService;

    @Autowired
    private PurchaseOrderLineCRUDService purchaseOrderLineCRUDService;

    public ShopProductCommissionCRUDService(UserAwareDAO<ShopProductCommission, Long> dao, ConverterService<ShopProductCommission, ShopProductCommissionVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShopProductCommissionDAO dao() {
        return (ShopProductCommissionDAO) super.dao();
    }

    public ShopProductCommission findByShopAndProduct(Shop shop, Product product) {
        return this.dao().findByShopAndProduct(shop, product);
    }

    public ShopProductCommissionVO initVO(ProductVO product) {
        ShopProductCommissionVO vo = this.initVO();
        vo.setProduct(product);
        return vo;
    }

    public ServiceResponse createCommission(SaleOrderLine line) {
        Shop shop = line.getSaleOrder().getShop();
        Account account = line.getSaleOrder().getAccount();

        if (!account.getType().getCommissionable()) {
            account = shop.getAccount();
            if (!account.getType().getCommissionable()) {
                return new ServiceResponse().setResponseCode(RESPONSE_CODE.E99).setMessage("Não foi encontrado conta que receba comissaão!");
            }
        }

        ShopProductCommission commission = this.dao().findByShopAndProduct(shop, line.getProduct());
        if (Objects.nonNull(commission)) {
            BigDecimal amount = this.calculateCommission(commission, line);
            PurchaseOrder purchaseOrder = this.purchaseOrderService.initEntity(shop, account, this.systemUser(), amount, PURCHASE_ORDER_STATUS.PENDING);
            String lineName = String.format("Comissão ref.: Pedido Venda [%s] Produto [%s]", line.getSaleOrder().getId(), line.getProduct().getName());
            PurchaseOrderLine purchaseLine = this.purchaseOrderLineCRUDService.initEntity(amount, lineName, purchaseOrder, PURCHASE_ORDER_STATUS.PENDING);
            purchaseOrder.getLines().add(purchaseLine);

            ServiceResponse result = this.purchaseOrderService.processSave(purchaseOrder, this.systemUser().getId());
            purchaseOrder = result.get("entity");
            return this.purchaseOrderService.activateCommissionOrder(purchaseOrder.getId(), this.systemUser(), line);
        } else {
            return new ServiceResponse().setMessage(String.format("Produto [%s] não possue comissão para a loja [%s]", line.getProduct().getName(), shop.getName()));
        }
    }

    private BigDecimal calculateCommission(ShopProductCommission commission, SaleOrderLine line) {
        BigDecimal result = BigDecimal.ZERO;

        if (Objects.nonNull(commission.getAmount()) && commission.getAmount().signum() == 1) {
            result = commission.getAmount();
        } else if (Objects.nonNull(commission.getPercentage()) && commission.getPercentage().signum() == 1) {
            result = line.getAmount().multiply(commission.getPercentage());
        }

        return result;
    }

    @Override
    public ServiceResponse validate(ShopProductCommissionVO vo, CRUD_OPERATION operation) {
        ServiceResponse response = new ServiceResponse();
        switch (operation) {
            case CREATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case DELETE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case READ:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
            case UPDATE:
                response.setResponseCode(RESPONSE_CODE.E00);
                break;
        }
        return response;
    }

    @Override
    protected Supplier<ShopProductCommission> initEntitySupplier() {
        return ShopProductCommission::new;
    }

    @Override
    protected Supplier<ShopProductCommissionVO> initVOSupplier() {
        return ShopProductCommissionVO::new;
    }

}
