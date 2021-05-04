package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.SaleOrderLineDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.vo.SaleOrderLineVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SMS_STATUS;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.SaleOrderSearchVO;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class SaleOrderLineCRUDService extends UserAwareCRUDService<SaleOrderLine, Long, SaleOrderLineVO, SaleOrderSearchVO> {

    public SaleOrderLineCRUDService(UserAwareDAO<SaleOrderLine, Long> dao, ConverterService<SaleOrderLine, SaleOrderLineVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public SaleOrderLineDAO dao() {
        return (SaleOrderLineDAO) super.dao();
    }

    public SaleOrderLine findByExternalCode(String externalCode) {
        return this.dao().findByExternalCode(externalCode);
    }
    
    public void setUserEmailStatusForId(EMAIL_STATUS status, Long id) {
        this.dao().setUserEmailStatusForId(status, id);
    }
    
    public void setSmsStatusForId(SMS_STATUS status, Long id) {
        this.dao().setSmsStatusForId(status, id);
    }

    public List<SaleOrderLine> findBySaleOrderId(Long id) {
        return this.dao().findBySaleOrderId(id);
    }

    public void activate(Long id) {
        SaleOrderLine line = this.findOne(id);
        if (Objects.nonNull(line)) {
            line.setStatus(SALE_ORDER_LINE_STATUS.ACTIVATED);
            this.dao().saveAndFlush(line, this.loggedUserId());
        }
    }

    @Override
    public ServiceResponse validate(SaleOrderLineVO vo, CRUD_OPERATION operation) {
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
    protected Supplier<SaleOrderLine> initEntitySupplier() {
        return SaleOrderLine::new;
    }

    @Override
    protected Supplier<SaleOrderLineVO> initVOSupplier() {
        return SaleOrderLineVO::new;
    }
}
