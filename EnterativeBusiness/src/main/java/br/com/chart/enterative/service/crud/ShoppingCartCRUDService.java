package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShoppingCartDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.ShoppingCart;
import br.com.chart.enterative.entity.vo.ShoppingCartVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ShoppingCartSearchVO;
import java.util.Date;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ShoppingCartCRUDService extends UserAwareCRUDService<ShoppingCart, Long, ShoppingCartVO, ShoppingCartSearchVO> {

    public ShoppingCartCRUDService(UserAwareDAO<ShoppingCart, Long> dao, ConverterService<ShoppingCart, ShoppingCartVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ShoppingCartDAO dao() {
        return (ShoppingCartDAO) super.dao();
    }

    public ShoppingCart findByCreatedById(Long userID) {
        return this.dao().findByCreatedById(userID);
    }

    @Override
    protected Supplier<ShoppingCart> initEntitySupplier() {
        return () -> {
            ShoppingCart entity = new ShoppingCart();
            entity.setAlteredAt(new Date());
            entity.setCreatedAt(new Date());
            return entity;
        };
    }

    @Override
    protected Supplier<ShoppingCartVO> initVOSupplier() {
        return ShoppingCartVO::new;
    }

    @Override
    public ServiceResponse validate(ShoppingCartVO vo, CRUD_OPERATION operation) {
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
