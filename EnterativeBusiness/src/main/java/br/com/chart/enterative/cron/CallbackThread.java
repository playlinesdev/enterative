package br.com.chart.enterative.cron;

import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.CallbackQueueDAO;
import br.com.chart.enterative.dao.EpayActivationDAO;
import br.com.chart.enterative.entity.CallbackQueue;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.EpayActivation;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.FluentService;
import br.com.chart.enterative.vo.CallbackDataVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import org.apache.http.client.fluent.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author William Leite
 */
public class CallbackThread extends Thread {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FluentService fluentService;

    @Autowired
    private CallbackQueueDAO queueDAO;

    @Autowired
    private BHNActivationDAO bhnActivationDAO;
    
    @Autowired
    private EpayActivationDAO epayActivationDAO;

    @Autowired
    private EnterativeUtils utils;

    private CallbackQueue queue;

    public CallbackThread() {
    }

    public void setQueue(CallbackQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        CALLBACK_STATUS status;
        String statusCodeClass = null;
        if (Objects.nonNull(queue.getBhnActivation())) {
            statusCodeClass = this.sendCallback(queue.getBhnActivation());
        } else if (Objects.nonNull(queue.getEpayActivation())) {
            statusCodeClass = this.sendCallback(queue.getEpayActivation());
        }
        
        switch (statusCodeClass) {
            // Successful
            case "2":
                status = CALLBACK_STATUS.DONE;
                break;
            // Informational
            case "1":
            // Redirection
            case "3":
            // Client Error
            case "4":
            // Server Error
            case "5":
                this.queueDAO.saveAndFlush(queue);
                status = CALLBACK_STATUS.ERROR;
                break;
            default:
                this.queueDAO.saveAndFlush(queue);
                status = CALLBACK_STATUS.ERROR;
                break;
        }

        if (Objects.nonNull(queue.getBhnActivation())) {
            this.bhnActivationDAO.setCallbackStatusForID(status, queue.getBhnActivation().getId());
        } else if (Objects.nonNull(queue.getEpayActivation())) {
            this.epayActivationDAO.setCallbackStatusForID(status, queue.getEpayActivation().getId());
        }
    }
    
    private String sendCallback(EpayActivation epayActivation) {
        CallbackDataVO vo = new CallbackDataVO();
        vo.setExternalCode(epayActivation.getExternalCode());
        vo.setResponseCode(epayActivation.getResponseCode());
        vo.setActivationStatus(epayActivation.getStatus());
        vo.setLastUpdate(new Date());
        
        String json = null;
        try {
            json = this.utils.toJSON(vo);
        } catch (JsonProcessingException ex) {
            log.error("Não foi possível converter para JSON: " + vo.getExternalCode());
        }
        
        if (Objects.isNull(json) || json.isEmpty()) {
            return "5";
        }

        String statusCode = Integer.toString(this.sendPOST(json, epayActivation));
        return statusCode.substring(0, 1);
    }

    private String sendCallback(BHNActivation bhnActivation) {
        CallbackDataVO vo = new CallbackDataVO();
        vo.setExternalCode(bhnActivation.getExternalCode());
        vo.setResponseCode(bhnActivation.getResponseCode());
        vo.setActivationStatus(bhnActivation.getStatus());
        vo.setLastUpdate(new Date());

        String json = null;
        try {
            json = this.utils.toJSON(vo);
        } catch (JsonProcessingException ex) {
            log.error("Não foi possível converter para JSON: " + vo.getExternalCode());
        }

        if (Objects.isNull(json) || json.isEmpty()) {
            return "5";
        }

        String statusCode = Integer.toString(this.sendPOST(json, bhnActivation));
        return statusCode.substring(0, 1);
    }
    
    private int sendPOST(String json, EpayActivation epayActivation) {
        int responseCode = -1;
        
        try {
            log.info("Enviando callback: [" + epayActivation.getCallbackurl() + "] " + json);
            Response response = this.fluentService.sendSimpleJSON(epayActivation.getCallbackurl(), json);
            responseCode = response.returnResponse().getStatusLine().getStatusCode();
            log.info("Enviado: " + responseCode);
        } catch (IOException ex) {
            this.log.error("Erro ao receber a resposta do cliente: codexterno=" + epayActivation.getExternalCode(), ex);
        } catch (Exception ex) {
            this.log.error("Erro ao enviar requisição ao cliente: codexterno=" + epayActivation.getExternalCode(), ex);
        }
        
        return responseCode;
    }

    private int sendPOST(String json, BHNActivation bhnActivation) {
        int responseCode = -1;

        try {
            log.info("Enviando callback: [" + bhnActivation.getCallbackurl() + "] " + json);
            Response response = this.fluentService.sendSimpleJSON(bhnActivation.getCallbackurl(), json);
            responseCode = response.returnResponse().getStatusLine().getStatusCode();
            log.info("Enviado: " + responseCode);
        } catch (IOException ex) {
            this.log.error("Erro ao receber a resposta do cliente: codexterno=" + bhnActivation.getExternalCode(), ex);
        } catch (Exception ex) {
            this.log.error("Erro ao enviar requisição ao cliente: codexterno=" + bhnActivation.getExternalCode(), ex);
        }

        return responseCode;
    }
}
