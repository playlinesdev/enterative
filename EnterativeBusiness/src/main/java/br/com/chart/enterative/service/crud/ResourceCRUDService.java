package br.com.chart.enterative.service.crud;

import br.com.chart.enterative.converter.base.ConverterService;
import br.com.chart.enterative.dao.ResourceDAO;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.entity.vo.ResourceVO;
import br.com.chart.enterative.enums.CRUD_OPERATION;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.service.base.UserAwareCRUDService;
import br.com.chart.enterative.vo.PageWrapper;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.search.ResourceSearchVO;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ResourceCRUDService extends UserAwareCRUDService<Resource, Long, ResourceVO, ResourceSearchVO> {

    public ResourceCRUDService(UserAwareDAO<Resource, Long> dao, ConverterService<Resource, ResourceVO> converter) {
        super(dao, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceDAO dao() {
        return (ResourceDAO) super.dao();
    }

    public List<Resource> findByServer(Server server) {
        return this.dao().findByServer(server);
    }

    public List<Resource> findByServerAndType(Server server, RESOURCE_TYPE type) {
        return this.dao().findByServerAndType(server, type);
    }

    @Override
    public PageWrapper<ResourceVO> retrieve(ResourceSearchVO searchForm, Pageable pageable, String url) {
        Page<Resource> page;
        if (Objects.nonNull(searchForm.getName()) && !searchForm.getName().isEmpty()) {
            if (Objects.nonNull(searchForm.getServer())) {
                page = this.dao().findByNameIgnoreCaseContainingAndServerIdOrderByName(searchForm.getName(), searchForm.getServer().getId(), pageable);
            } else {
                page = this.dao().findByNameIgnoreCaseContainingOrderByName(searchForm.getName(), pageable);
            }
        } else {
            if (Objects.nonNull(searchForm.getServer())) {
                page = this.dao().findByServerIdOrderByName(searchForm.getServer().getId(), pageable);
            } else {
                page = this.dao().findAll(pageable);
            }
        }

        return new PageWrapper<>(page.map(this.converter()::convert), url);
    }

    @Override
    protected Supplier<Resource> initEntitySupplier() {
        return Resource::new;
    }

    @Override
    protected Supplier<ResourceVO> initVOSupplier() {
        return ResourceVO::new;
    }

    @Override
    public ServiceResponse validate(ResourceVO vo, CRUD_OPERATION operation) {
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
