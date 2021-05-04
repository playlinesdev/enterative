package br.com.chart.enterative.web.listener;

import br.com.chart.enterative.entity.SaleOrder;
import br.com.chart.enterative.entity.SaleOrderLine;
import br.com.chart.enterative.entity.vo.ProductVO;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.service.EmailService;
import br.com.chart.enterative.converter.base.HelperConverterService;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.event.PersonallyActivationEmailEvent;
import br.com.chart.enterative.service.base.UserAwareComponent;
import br.com.chart.enterative.service.crud.SaleOrderCRUDService;
import br.com.chart.enterative.service.crud.SaleOrderLineCRUDService;
import br.com.chart.enterative.vo.PersonallyActivationEmailVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author William Leite
 */
@Component
public class PersonallyActivationEmailEventListener extends UserAwareComponent implements ApplicationListener<PersonallyActivationEmailEvent> {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SaleOrderCRUDService saleOrderCRUDService;

    @Autowired
    private SaleOrderLineCRUDService saleOrderLineCRUDService;

    @Autowired
    private HelperConverterService helperConverterService;

    @Override
    public void onApplicationEvent(PersonallyActivationEmailEvent event) {
        try {
            this.log("Sending email: Sale Order [" + event.getSaleOrderID() + "]");
            
            SaleOrder saleOrder = this.saleOrderCRUDService.findByIdEager(event.getSaleOrderID());
            
            if (!saleOrder.getLines().stream().anyMatch(l -> l.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN)) {
                return;
            }
            
            String subject = "virtual.activation.subject";
            String from = this.parameterDAO.get(ENVIRONMENT_PARAMETER.DEFAULT_FROM_ADDRESS);
            String template = "mail/personallyActivation";
            List<PersonallyActivationEmailVO> emailLines = new ArrayList<>();

            List<SaleOrderLine> lines = this.saleOrderLineCRUDService.findBySaleOrderId(saleOrder.getId());
            lines.stream().filter(l -> l.getProduct().getActivationProcess() == ACTIVATION_PROCESS.BHN).forEach(l -> {
                String pin = "";

                PersonallyActivationEmailVO line = new PersonallyActivationEmailVO();
                
                ProductVO vo = new ProductVO();
                vo.setDisplayName(l.getProduct().getDisplayName());
                line.setProduct(vo);
                line.setOrderId(saleOrder.getId());
                line.setStatus(saleOrder.getStatus());
                emailLines.add(line);
            });

            Map<String, Object> context = new HashMap<>();
            context.put("subject", subject);
            context.put("lines", emailLines);
            
            Locale locale;
            
            if (Objects.nonNull(saleOrder.getLocale()) && !saleOrder.getLocale().isEmpty()) {
                locale = Locale.forLanguageTag(saleOrder.getLocale());
            } else {
                locale = this.locale();
            }            

            String translatedUniqueSubject = String.format("%s - %s", this.helperConverterService.getMessage(subject, locale), saleOrder.getId());
            
            try {
                this.emailService.sendSimpleMail(context, translatedUniqueSubject, from, saleOrder.getCreatedBy().getEmail(), locale, template);
                this.saleOrderCRUDService.setEmailStatusForId(EMAIL_STATUS.SENT, saleOrder.getId());
                this.log("Email Sent!");
            } catch (MessagingException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
