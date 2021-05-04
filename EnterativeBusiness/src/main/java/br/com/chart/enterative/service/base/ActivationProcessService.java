package br.com.chart.enterative.service.base;

import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.Server;
import br.com.chart.enterative.helper.LogService;
import br.com.chart.enterative.vo.ActiveResourceVO;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSRequestLine;
import java.util.List;

/**
 *
 * @author William Leite
 */
public abstract class ActivationProcessService extends LogService {
    
    public abstract Server executeEcho(List<Server> activeServers);
    
    public abstract ActiveResourceVO retrieveResource(Server server);
    
    public abstract boolean validateServer(Server server);
    
    public abstract String process();
    
    public abstract void activate(WSRequest request, WSRequestLine l, Merchant merchant, Long systemUserID);
}
