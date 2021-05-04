package br.com.chart.enterative.mock.server.service;

import br.com.chart.enterative.entity.transacao.Transacao;
import br.com.chart.enterative.entity.ws.bhn.Resposta;
import br.com.chart.enterative.mock.server.entity.enums.SERVER_PARAMS;
import br.com.chart.enterative.mock.server.component.BaseComponent;
import br.com.chart.enterative.mock.server.utils.MockUtils;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class NetworkResourceService extends BaseComponent {

    @Autowired
    private MockUtils mockUtils;

    public Resposta process(Transacao transacao) {
        this.printVerbose("<network>");
        Resposta result = null;

        if (Objects.nonNull(transacao) && Objects.equals(transacao.getTransaction().getNetworkManagementCode(), "301")) {
            String echoSuccessful = this.mockUtils.getParamsValue(SERVER_PARAMS.ECHO_SUCCESSFUL);
            if (Objects.equals(echoSuccessful, "1")) {
                this.printVerbose("--> successful ECHO");
                result = mockUtils.createECHOSuccessful();
            } else {
                this.printVerbose("--> failed ECHO");
                result = mockUtils.createECHOFailure();
            }
        } else {
            this.printVerbose("--> failed network");
        }

        this.printVerbose("</network>");
        return result;
    }
}
