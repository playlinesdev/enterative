package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.repository.ResourceRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ResourceDAO extends UserAwareDAO<Resource, Long> {

    public ResourceDAO(BaseRepository<Resource, Long> repository) {
        super(repository);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceRepository repository() {
        return (ResourceRepository) super.repository();
    }

    public List<Resource> findByServer(Server server) {
        return this.repository().findByServer(server);
    }

    public List<Resource> findByServerAndType(Server server, RESOURCE_TYPE type) {
        return this.repository().findByServerAndType(server, type);
    }

    public Page<Resource> findByNameIgnoreCaseContainingAndServerIdOrderByName(String name, Long id, Pageable pageable) {
        return this.repository().findByNameIgnoreCaseContainingAndServerIdOrderByName(name, id, pageable);
    }

    public Page<Resource> findByServerIdOrderByName(Long id, Pageable pageable) {
        return this.repository().findByServerIdOrderByName(id, pageable);
    }

}
