package br.com.chart.enterative.mock.server.utils;

import br.com.chart.enterative.entity.transacao.Transaction;
import br.com.chart.enterative.entity.ws.bhn.Response;
import br.com.chart.enterative.entity.ws.bhn.Resposta;
import br.com.chart.enterative.mock.server.entity.ServerParam;
import br.com.chart.enterative.mock.server.entity.enums.SERVER_PARAMS;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.chart.enterative.mock.server.dao.repository.ServerParamRepository;

/**
 *
 * @author William Leite
 */
@Service
public class MockUtils {

    @Autowired
    private ServerParamRepository paramRepository;

    private Resposta createECHO(String responseCode) {
        Transaction t = new Transaction();
        t.setResponseCode(responseCode);

        Response r1 = new Response();
        r1.setTransaction(t);

        Resposta r = new Resposta();
        r.setResponse(r1);

        return r;
    }

    public Resposta createECHOFailure() {
        return this.createECHO("99");
    }

    public Resposta createECHOSuccessful() {
        return this.createECHO("00");
    }

    public String getParamsValue(SERVER_PARAMS name) {
        ServerParam param = this.paramRepository.findByName(name);
        if (Objects.nonNull(param)) {
            return param.getValue();
        } else {
            return null;
        }
    }
}
