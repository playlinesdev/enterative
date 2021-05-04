package br.com.chart.enterative.service.activation;

import br.com.chart.enterative.dao.BHNActivationDAO;
import br.com.chart.enterative.dao.BHNVoucherDAO;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNVoucher;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.converter.WSRequestConverterService;
import br.com.chart.enterative.service.activationprocess.BHNActivationProcess;
import br.com.chart.enterative.service.activationprocess.EpayActivationProcess;
import br.com.chart.enterative.vo.ServiceResponse;
import br.com.chart.enterative.vo.WSRequest;
import java.util.Objects;

import br.com.chart.enterative.vo.WSRequestLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class LocalActivationService extends UserAwareComponent {

    @Autowired
    private BHNActivationDAO activationDAO;

    @Autowired
    private BHNVoucherDAO bhnVoucherDAO;

    @Autowired
    private WSRequestConverterService wsRequestConverterService;
    
    @Autowired
    private BHNActivationProcess bhnActivationProcess;
    
    @Autowired
    private EpayActivationProcess epayActivationProcess;

    private ServiceResponse __verifyStatus(BHNActivation activation) {
        ServiceResponse result = new ServiceResponse();
        RESPONSE_CODE validationResult = RESPONSE_CODE.E00;
        if (Objects.nonNull(activation)) {
            String resposta = activation.getResponseCode();
            if (Objects.nonNull(resposta) && !resposta.isEmpty()) {
                result.put("respostaAux", RESPONSE_CODE.get(resposta));
            } else {
                result.put("respostaAux", null);
            }
            result.put("statusAtivacao", activation.getStatus());
        } else {
            validationResult = RESPONSE_CODE.E10;
        }

        result.put("resposta", validationResult);
        return result;
    }

    public ServiceResponse verifyStatus(Merchant merchant, String shop, String terminal, String codexterno) {
        BHNActivation activation = this.activationDAO.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shop, terminal, codexterno);
        return this.__verifyStatus(activation);
    }

    public ServiceResponse activateCard(WSRequest request, Merchant merchant) {
        ServiceResponse result = new ServiceResponse();

        request.getLines().forEach(l -> {
            switch (l.getActivationProcess()) {
                case BHN:
                    this.bhnActivationProcess.activate(request, l, merchant, this.systemUserId());
                    break;
                case EPAY:
                    this.epayActivationProcess.activate(request, l, merchant, this.systemUserId());
                    break;
                default:
                    l.setActivationStatus(null);
                    l.setResponseAux(null);
                    l.setResponse(RESPONSE_CODE.E32);
                    break;
            }
        });

        RESPONSE_CODE response = request.getLines().stream().map(WSRequestLine::getResponse).filter(r -> r != RESPONSE_CODE.E00).findFirst().orElse(RESPONSE_CODE.E00);
        result.put("request", request);
        result.put("response", response);

        return result;
    }
}
