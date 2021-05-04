package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ShoppingCartLine;
import br.com.chart.enterative.entity.vo.ShoppingCartLineVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShoppingCartLineSearchVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartLineCRUDService extends UserAwareCRUDService<ShoppingCartLine, Long, ShoppingCartLineVO, ShoppingCartLineSearchVO> {

    public ShoppingCartLineCRUDService(UserAwareDAO<ShoppingCartLine, Long> dao, ConverterService<ShoppingCartLine, ShoppingCartLineVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<ShoppingCartLine> initEntitySupplier() {
        return () -> {
            ShoppingCartLine line = new ShoppingCartLine();
            line.setAlteredAt(new Date());
            line.setCreatedAt(new Date());
            line.setQuantity(BigDecimal.ONE);
            return line;
        };
    }

    @Override
    protected Supplier<ShoppingCartLineVO> initVOSupplier() {
        return ShoppingCartLineVO::new;
    }

    @Override
    public ServiceResponse validate(ShoppingCartLineVO vo, CRUD_OPERATION operation) {
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

}
