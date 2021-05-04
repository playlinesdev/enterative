package br.com.chart.enterative.web.listener;

import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.entity.BHNActivation;
import br.com.chart.enterative.entity.BHNTransaction;
import br.com.chart.enterative.entity.Merchant;
import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.SMS_STATUS;
import br.com.chart.enterative.enums.TRANSACTION_DIRECTION;
import br.com.chart.enterative.event.VirtualActivationEmailEvent;
import br.com.chart.enterative.service.EmailService;
import br.com.chart.enterative.service.SMSService;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.BHNActivationCRUDService;
import br.com.chart.enterative.service.crud.BHNTransactionCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderLineCRUDService;
import br.com.chart.enterative.vo.VirtualActivationEmailVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class VirtualActivationEmailEventListener extends UserAwareComponent implements ApplicationListener<VirtualActivationEmailEvent> {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SMSService smsService;

    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;

    @Autowired
    private SaleOrderLineCRUDService saleOrderLineCRUDService;

    @Autowired
    private BHNActivationCRUDService bhnActivationCRUDService;

    @Autowired
    private BHNTransactionCRUDService bhnTransactionCRUDService;

    @Autowired
    private HelperConverterService helperConverterService;
    
    @Autowired
    private EnvParameterDAO paramDAO;

    @Override
    public void onApplicationEvent(VirtualActivationEmailEvent event) {
        try {
            this.log("Sending email: Sale Order [" + event.getSaleOrderID() + "]");

            SaleOrder saleOrder = this.saleOrderCRUDService.findByIdEager(event.getSaleOrderID());

            if (!saleOrder.getLines().stream().anyMatch(l -> l.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN)) {
                return;
            }

            Merchant merchant = saleOrder.getShop().getMerchant();
            String terminal = saleOrder.getCreatedBy().getTerminal();
            String shopCode = saleOrder.getShop().getCode();

            String subject = "virtual.activation.subject";
            String messagePin = "virtual.activation.messagePin";
            String from = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_FROM_ADDRESS);
            String template = "mail/virtualActivation";
            List<VirtualActivationEmailVO> emailLines = new ArrayList<>(0);
            Map<String, List<VirtualActivationEmailVO>> userEmailLines = new HashMap<>(0);

            List<SaleOrderLine> lines = this.saleOrderLineCRUDService.findBySaleOrderId(saleOrder.getId());
            lines.stream().filter(l -> l.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN).forEach(l -> {
                String pin = "";
                String cardNumber = "--";
                String terms = "";
                String redemption = "";

                String externalCode = l.getExternalCode();
                BHNActivation activation = this.bhnActivationCRUDService.findByMerchantAndShopCodeAndTerminalAndExternalCode(merchant, shopCode, terminal, externalCode);
                if (Objects.nonNull(activation)) {
                    pin = activation.getVoucher().getPin();

                    List<BHNTransaction> transactions = this.bhnTransactionCRUDService.findByBhnActivationId(activation.getId());
                    Optional<BHNTransaction> transaction = transactions.stream().filter((t) -> t.getDirection() == TRANSACTION_DIRECTION.RETURN).findAny();

                    if (transaction.isPresent()) {
                        cardNumber = transaction.get().getActivationAccountNumber();
                        terms = transaction.get().getTermsAndConditions();
                        redemption = transaction.get().getLine();
                    }
                }

                if (Objects.equals(l.getProduct().getSendsSMS(), Boolean.TRUE)) {
                    String apikey = this.paramDAO.get(ENVIRONMENT_PARAMETER.SMS_APIKEY);
                    String apisecret = this.paramDAO.get(ENVIRONMENT_PARAMETER.SMS_APISECRET);
                    String msg;
                    
                    if (StringUtils.isAllBlank(l.getProduct().getSmsTemplate())) {
                        msg = String.format("Seu Cartão foi ativado com sucesso! PIN: %s. Enterative, seu parceiro digital.", pin);
                    } else {
                        msg = l.getProduct().getSmsTemplate().replace("{pin}", pin.replace("-", StringUtils.EMPTY));
                    }
                    
                    this.smsService.send(apikey, apisecret, "Enterative", l.getUserCellphone(), msg);
                    this.saleOrderLineCRUDService.setSmsStatusForId(SMS_STATUS.SENT, l.getId());
                }

                VirtualActivationEmailVO line = new VirtualActivationEmailVO();
                line.setPin(pin);
                line.setCardNumber(cardNumber);
                line.setTerms(terms);
                line.setRedemption(redemption);
                line.setSaleOrderLineId(l.getId());

                ProductVO vo = new ProductVO();
                vo.setDisplayName(l.getProduct().getDisplayName());
                line.setProduct(vo);
                emailLines.add(line);

                if (!Objects.equals(l.getUserEmail(), saleOrder.getCreatedBy().getEmail())) {
                    if (!userEmailLines.containsKey(l.getUserEmail())) {
                        userEmailLines.put(l.getUserEmail(), new ArrayList<>(0));
                    }

                    userEmailLines.get(l.getUserEmail()).add(line);
                }
            });

            Locale locale;

            if (Objects.nonNull(saleOrder.getLocale()) && !saleOrder.getLocale().isEmpty()) {
                locale = Locale.forLanguageTag(saleOrder.getLocale());
            } else {
                locale = this.locale();
            }

            String translatedUniqueSubject = String.format("%s - %s", this.helperConverterService.getMessage(subject, locale), saleOrder.getId());

            // Main Email
            Map<String, Object> context = new HashMap<>(0);
            context.put("subject", subject);
            context.put("lines", emailLines);

            try {
                this.emailService.sendSimpleMail(context, translatedUniqueSubject, from, saleOrder.getCreatedBy().getEmail(), locale, template);
                this.saleOrderCRUDService.setEmailStatusForId(EMAIL_STATUS.SENT, saleOrder.getId());
                this.log("Main Email Sent!");
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }

            // User Emails
            userEmailLines.entrySet().stream().forEach(entry -> {
                context.put("lines", entry.getValue());
                try {
                    this.emailService.sendSimpleMail(context, translatedUniqueSubject, from, entry.getKey(), locale, template);
                    entry.getValue().stream().map(VirtualActivationEmailVO::getSaleOrderLineId).forEach(id -> this.saleOrderLineCRUDService.setUserEmailStatusForId(EMAIL_STATUS.SENT, id));
                    this.log("User Email Sent! [%s]", entry.getKey());
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
