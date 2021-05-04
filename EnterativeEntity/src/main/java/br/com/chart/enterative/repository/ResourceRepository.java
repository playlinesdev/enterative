package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author William Leite
 */
public interface ResourceRepository extends UserAwareRepository<Resource, Long> {

    public List<Resource> findByServer(Server servidor);

    public List<Resource> findByServerAndType(Server servidor, RESOURCE_TYPE type);

    public Page<Resource> findByNameIgnoreCaseContainingAndServerIdOrderByName(String name, Long id, Pageable pageable);

    public Page<Resource> findByServerIdOrderByName(Long id, Pageable pageable);

}
