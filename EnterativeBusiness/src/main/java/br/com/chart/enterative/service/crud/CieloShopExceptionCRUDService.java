package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ShopDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.CieloShopException;
import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.vo.CieloShopExceptionVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.exception.CRUDServiceException;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.CieloShopExceptionSearchVO;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class CieloShopExceptionCRUDService extends UserAwareCRUDService<CieloShopException, Long, CieloShopExceptionVO, CieloShopExceptionSearchVO> {

    @Autowired
    private ShopDAO shopDAO;
    
    public CieloShopExceptionCRUDService(UserAwareDAO<CieloShopException, Long> dao, ConverterService<CieloShopException, CieloShopExceptionVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<CieloShopException> initEntitySupplier() {
        return CieloShopException::new;
    }

    @Override
    protected Supplier<CieloShopExceptionVO> initVOSupplier() {
        return CieloShopExceptionVO::new;
    }

    @Override
    public ServiceResponse processSave(CieloShopExceptionVO vo, Long user) throws CRUDServiceException {
        Shop shop = this.shopDAO.findOne(vo.getShop().getId());
        vo.setName(shop.getName());
        return super.processSave(vo, user);
    }
    
    

    @Override
    public ServiceResponse validate(CieloShopExceptionVO vo, CRUD_OPERATION operation) {
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
