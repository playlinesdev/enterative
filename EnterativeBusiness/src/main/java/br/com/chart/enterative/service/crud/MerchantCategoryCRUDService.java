package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.MerchantCategory;
import br.com.chart.enterative.entity.vo.MerchantCategoryVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.MerchantCategorySearchVO;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class MerchantCategoryCRUDService extends UserAwareCRUDService<MerchantCategory, Long, MerchantCategoryVO, MerchantCategorySearchVO> {

    public MerchantCategoryCRUDService(UserAwareDAO<MerchantCategory, Long> dao, ConverterService<MerchantCategory, MerchantCategoryVO> converter) {
        super(dao, converter);
    }

    public List<MerchantCategoryVO> findOrderByNameVO() {
        return this.findAllVOSorted(Comparator.comparing(MerchantCategoryVO::getName)).collect(Collectors.toList());
    }

    @Override
    protected Supplier<MerchantCategory> initEntitySupplier() {
        return MerchantCategory::new;
    }

    @Override
    protected Supplier<MerchantCategoryVO> initVOSupplier() {
        return MerchantCategoryVO::new;
    }

    @Override
    public ServiceResponse validate(MerchantCategoryVO vo, CRUD_OPERATION operation) {
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
