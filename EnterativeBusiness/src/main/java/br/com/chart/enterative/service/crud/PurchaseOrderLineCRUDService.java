package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.PurchaseOrderLineDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.PurchaseOrder;
import br.com.chart.enterative.entity.PurchaseOrderLine;
import br.com.chart.enterative.entity.vo.PurchaseOrderLineVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.PURCHASE_ORDER_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.PurchaseOrderLineSearchVO;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class PurchaseOrderLineCRUDService extends UserAwareCRUDService<PurchaseOrderLine, Long, PurchaseOrderLineVO, PurchaseOrderLineSearchVO> {

    public PurchaseOrderLineCRUDService(UserAwareDAO<PurchaseOrderLine, Long> dao, ConverterService<PurchaseOrderLine, PurchaseOrderLineVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PurchaseOrderLineDAO dao() {
        return (PurchaseOrderLineDAO) super.dao();
    }

    public void setStatusForPurchaseOrderID(PURCHASE_ORDER_STATUS status, Long id) {
        this.dao().setStatusForPurchaseOrderID(status, id);
    }

    public void deleteByID(List<Long> ids) {
        this.dao().deleteByID(ids);
    }

    @Override
    public ServiceResponse validate(PurchaseOrderLineVO vo, CRUD_OPERATION operation) {
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

    public PurchaseOrderLine initEntity(BigDecimal amount, String name, PurchaseOrder purchaseOrder, PURCHASE_ORDER_STATUS status) {
        PurchaseOrderLine line = new PurchaseOrderLine();
        line.setAmount(amount);
        line.setName(name);
        line.setPurchaseOrder(purchaseOrder);
        line.setStatus(status);
        return line;
    }

    @Override
    protected Supplier<PurchaseOrderLine> initEntitySupplier() {
        return () -> {
            PurchaseOrderLine entity = new PurchaseOrderLine();
            entity.setStatus(PURCHASE_ORDER_STATUS.PENDING);
            return entity;
        };
    }

    @Override
    protected Supplier<PurchaseOrderLineVO> initVOSupplier() {
        return () -> {
            PurchaseOrderLineVO vo = new PurchaseOrderLineVO();
            vo.setStatus(PURCHASE_ORDER_STATUS.PENDING);
            vo.setAmount(BigDecimal.ZERO);
            return vo;
        };
    }
}
