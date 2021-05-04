package br.com.chart.enterative.mock.server.controller;

import br.com.chart.enterative.entity.transacao.Transacao;
import br.com.chart.enterative.entity.ws.bhn.Resposta;
import br.com.chart.enterative.mock.server.component.BaseComponent;
import br.com.chart.enterative.mock.server.service.NetworkResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author William Leite
 */
@Controller
public class NetworkResourceController extends BaseComponent {

    @Autowired
    private NetworkResourceService networkService;

    @RequestMapping(path = "network", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Resposta network(@RequestBody Transacao transacao) {
        return this.networkService.process(transacao);
    }
}