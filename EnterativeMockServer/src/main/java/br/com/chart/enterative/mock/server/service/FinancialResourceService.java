package br.com.chart.enterative.mock.server.service;

import br.com.chart.enterative.entity.transacao.Transacao;
import br.com.chart.enterative.entity.ws.bhn.Response;
import br.com.chart.enterative.entity.ws.bhn.Resposta;
import br.com.chart.enterative.mock.server.entity.ActivationCard;
import br.com.chart.enterative.mock.server.component.BaseComponent;
import br.com.chart.enterative.mock.server.dao.repository.ActivationCardRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author William Leite
 */
@Service
public class FinancialResourceService extends BaseComponent {

    @Autowired
    private ActivationCardRepository cardRepository;

    public Resposta process(@RequestBody Transacao transacao) {
        this.printVerbose("<transaction>");

        Resposta resposta = new Resposta();
        Response response = new Response();

        response.setHeader(transacao.getHeader());
        response.setTransaction(transacao.getTransaction());
        resposta.setResponse(response);

        String processingCode = transacao.getTransaction().getProcessingCode();
        String pointOfServiceEntryMode = transacao.getTransaction().getPointOfServiceEntryMode();

        if (Objects.nonNull(processingCode) && !processingCode.isEmpty() && Objects.nonNull(pointOfServiceEntryMode) && !pointOfServiceEntryMode.isEmpty()) {
            if (Objects.equals(processingCode, "725400") && Objects.equals(pointOfServiceEntryMode, "031")) {
                if (Objects.nonNull(transacao.getTransaction()) && Objects.nonNull(transacao.getTransaction().getAdditionalTxnFields())) {
                    String ean = transacao.getTransaction().getAdditionalTxnFields().getProductId();
                    String number = transacao.getTransaction().getPrimaryAccountNumber();

                    if (Objects.nonNull(ean) && !ean.isEmpty() && Objects.nonNull(number) && !number.isEmpty()) {
                        this.printVerbose("--> Processing [%s][%s]", ean, number);

                        ActivationCard card = this.cardRepository.findByEanStartingWithAndCardNumber(ean, number);

                        if (Objects.nonNull(card)) {
                            this.printVerbose("--> Response Code: [%s]", card.getResponseCode());
                            resposta.getResponse().getHeader().getDetails().setStatusCode(card.getResponseCode());
                            resposta.getResponse().getTransaction().setResponseCode(card.getResponseCode());
                            resposta.getResponse().getTransaction().setAuthIdentificationResponse("000000");
                            resposta.getResponse().getTransaction().getAdditionalTxnFields().setBalanceAmount("C000000000000");
                            resposta.getResponse().getTransaction().getAdditionalTxnFields().setTransactionUniqueId("ABCDEFGHIJKL0123456789MNOP");
                            resposta.getResponse().getTransaction().getAdditionalTxnFields().setCorrelatedTransactionUniqueId("ABCDEFGHIJKL0123456789MNOP");

                            if (Objects.equals(card.getOneTime(), "1")) {
                                this.printVerbose("--> One-time removal");
                                this.cardRepository.deleteByID(card.getId());
                            }
                        } else {
                            this.printVerbose("--> Card not found: [%s][%s]", ean, number);
                            resposta.getResponse().getTransaction().setResponseCode("99");
                        }
                    } else {
                        this.printVerbose("--> Invalid card: [%s][%s]", ean, number);
                        resposta.getResponse().getTransaction().setResponseCode("99");
                    }
                } else {
                    this.printVerbose("--> Invalid request");
                    resposta.getResponse().getTransaction().setResponseCode("99");
                }
            } else if (Objects.equals(processingCode, "745400") && Objects.equals(pointOfServiceEntryMode, "041")) {
                String ean = transacao.getTransaction().getAdditionalTxnFields().getProductId();

                if (Objects.nonNull(ean) && !ean.isEmpty()) {
                    this.printVerbose("--> Processing [%s]", ean);

                    resposta.getResponse().getHeader().getDetails().setStatusCode("00");
                    resposta.getResponse().getTransaction().setResponseCode("00");
                    resposta.getResponse().getTransaction().setAuthIdentificationResponse("000000");
                    resposta.getResponse().getTransaction().getAdditionalTxnFields().setBalanceAmount("C000000000000");
                    resposta.getResponse().getTransaction().getAdditionalTxnFields().setTransactionUniqueId("ABCDEFGHIJKL0123456789MNOP");
                    resposta.getResponse().getTransaction().getAdditionalTxnFields().setCorrelatedTransactionUniqueId("ABCDEFGHIJKL0123456789MNOP");
                    resposta.getResponse().getTransaction().getAdditionalTxnFields().setRedemptionPin("RANDOMPIN");
                }
            }
        } else {
            this.printVerbose("--> Invalid request");
            resposta.getResponse().getTransaction().setResponseCode("99");
        }

        this.printVerbose("</transaction>");
        return resposta;
    }
}
