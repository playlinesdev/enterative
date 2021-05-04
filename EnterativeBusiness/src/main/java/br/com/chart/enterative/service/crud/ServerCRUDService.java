package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.entity.vo.ServerVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ServerSearchVO;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ServerCRUDService extends UserAwareCRUDService<Server, Long, ServerVO, ServerSearchVO> {

    public ServerCRUDService(UserAwareDAO<Server, Long> dao, ConverterService<Server, ServerVO> converter) {
        super(dao, converter);
    }

    @Override
    protected Supplier<Server> initEntitySupplier() {
        return Server::new;
    }

    @Override
    protected Supplier<ServerVO> initVOSupplier() {
        return ServerVO::new;
    }

    @Override
    public ServiceResponse validate(ServerVO vo, CRUD_OPERATION operation) {
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
