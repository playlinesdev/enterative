package br.com.chart.enterative.service;

import br.com.chart.enterative.dao.ResourceDAO;
import br.com.chart.enterative.dao.ServerDAO;
import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.RESOURCE_TYPE;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.service.activationprocess.BHNActivationProcess;
import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.vo.ActiveResourceVO;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class ECHOService extends UserAwareComponent {

    @Autowired
    private ServerDAO serverDAO;

    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private BHNActivationProcess bhnActivationProcess;

    @Autowired
    private EpayActivationProcess epayActivationProcess;

    public ActiveResourceVO retrieveActiveResource(ACTIVATION_PROCESS process) {
        ActiveResourceVO activeResource = null;
        Server server = this.retrieveActiveServer(process);

        if (Objects.nonNull(server) && this.validateServer(process, server)) {
            activeResource = this.retrieveResource(process, server);
        }

        return activeResource;
    }

    public Server retrieveActiveServer(ACTIVATION_PROCESS process) {
        Server activeServer = null;
        try {
            List<Server> activeServers = this.serverDAO.findByStatusAndActivationProcessOrderBySequenceAsc(STATUS.ACTIVE, process);

            if (Objects.nonNull(activeServers) && !activeServers.isEmpty()) {
                switch (process) {
                    case BHN:
                        activeServer = this.bhnActivationProcess.executeEcho(activeServers);
                        break;
                    case EPAY:
                        activeServer = this.epayActivationProcess.executeEcho(activeServers);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.log("Erro ao processar o ECHO : " + e.getMessage());
        }

        return activeServer;
    }

    public boolean validateServer(ACTIVATION_PROCESS process, Server server) {
        switch (process) {
            case BHN:
                return this.bhnActivationProcess.validateServer(server);
            case EPAY:
                return this.epayActivationProcess.validateServer(server);
        }
        return false;
    }

    public ActiveResourceVO retrieveResource(ACTIVATION_PROCESS process, Server server) {
        switch (process) {
            case BHN:
                return this.bhnActivationProcess.retrieveResource(server);
            case EPAY:
                return this.epayActivationProcess.retrieveResource(server);
        }
        return null;
    }

    public Resource retrieveResource(Server server, final RESOURCE_TYPE type) {
        List<Resource> resources = this.resourceDAO.findByServer(server);
        Optional<Resource> resource = resources.stream().filter(r -> r.getType() == type).findFirst();
        return resource.orElse(null);
    }
}
