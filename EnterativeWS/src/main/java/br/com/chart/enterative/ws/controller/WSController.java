package br.com.chart.enterative.ws.controller;

import br.com.chart.enterative.entity.Shop;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.UserRole;
import br.com.chart.enterative.entity.vo.PurchaseOrderVO;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.ACTIVATION_TYPE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.helper.EnterativeUtils;
import br.com.chart.enterative.service.activation.LocalActivationService;
import br.com.chart.enterative.service.activation.RequestValidationService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.*;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.WSPurchaseInfo;
import br.com.chart.enterative.vo.WSRequest;
import br.com.chart.enterative.vo.WSUserInfo;
import br.com.chart.enterative.ws.config.TokenAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Expõe servico de Ativacao
 *
 * @author Cristhiano Roberto
 */
@RestController
@RequestMapping("/ws")
public class WSController extends UserAwareComponent {

    @Autowired
    private RequestValidationService requestValidationService;

    @Autowired
    private LocalActivationService localActivationService;

    @Autowired
    private TokenAuthenticationProvider authenticationProvider;

    @Autowired
    private AccountCRUDService accountService;

    @Autowired
    private EnterativeUtils utils;

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public WSRequest status(@RequestBody WSRequest request) {
        RESPONSE_CODE validationResult = this.requestValidationService.structural(request, this.loggedUser(), false);

        if (validationResult == RESPONSE_CODE.E00) {
            request.getLines().stream().forEach(l -> {
                ServiceResponse result = this.localActivationService.verifyStatus(this.loggedUser().getShop().getMerchant(), request.getShop(), request.getTerminal(), l.getExternalCode());
                l.setResponseAux((RESPONSE_CODE) result.get("respostaAux"));
                l.setActivationStatus((ACTIVATION_STATUS) result.get("statusAtivacao"));
                l.setResponse((RESPONSE_CODE) result.get("resposta"));
            });
        } else {
            request.getLines().stream().forEach(l -> {
                l.setResponse(validationResult);
            });
        }

        return request;
    }

    @RequestMapping(value = "/ativacao", method = RequestMethod.POST)
    public WSRequest ativar(@RequestBody WSRequest request) {
        try {
            String json = this.utils.toJSON(request);
            this.log("Request: %s", json);

        } catch (Exception e) {
            this.log("Request inválido");
        }

        boolean needsBarcode = request.getActivationType() == ACTIVATION_TYPE.PHYSICAL;
        RESPONSE_CODE validationResult = this.requestValidationService.structural(request, loggedUser(), needsBarcode);

        if (validationResult == RESPONSE_CODE.E00) {
            validationResult = this.requestValidationService.consistency(request);
            if (validationResult == RESPONSE_CODE.E00) {
                ServiceResponse result = this.localActivationService.activateCard(request, loggedUser().getShop().getMerchant());
                request = result.get("request");
                validationResult = result.get("response");
            }
        }

        request.setResponse(validationResult);

        try {
            String json = this.utils.toJSON(request);
            this.log("Response: %s", json);
        } catch (Exception e) {
            this.log("Response inválido");
        }
        return request;
    }
}
