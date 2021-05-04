package br.com.chart.enterative.web.controller;

import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.CallbackQueueService;
import br.com.chart.enterative.helper.LogService;
import br.com.chart.enterative.vo.CallbackDataVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author William Leite
 */
@Controller
public class CallbackController extends LogService {

    @Autowired
    private CallbackQueueService callbackService;

    @Autowired
    private EnterativeUtils utils;

    private static final String CALLBACK_URL = "callback";

    @RequestMapping(value = CALLBACK_URL, method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public ResponseEntity callback(@RequestBody CallbackDataVO vo) throws JsonProcessingException {
        this.log("Callback JSON: " + this.utils.toJSON(vo));
        HttpStatus result = this.callbackService.receiveCallback(vo);
        this.log("Callback recebido! " + vo.getExternalCode());
        return new ResponseEntity(result);
    }

    @RequestMapping(value = "callback/check/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String checkCallback(@PathVariable("id") Long id) {
        return this.callbackService.checkCallback(id);
    }
}
