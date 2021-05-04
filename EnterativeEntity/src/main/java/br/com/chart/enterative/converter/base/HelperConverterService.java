package br.com.chart.enterative.converter.base;

import br.com.chart.enterative.helper.LogService;
import br.com.chart.enterative.vo.ServiceResponse;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class HelperConverterService extends LogService {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String id) {
        Locale locale = this.locale();
        try {
            return messageSource.getMessage(id, null, locale);
        } catch (NoSuchMessageException ex) {
            return id;
        }
    }

    public String getMessage(String id, String locale) {
        if (StringUtils.isBlank(locale)) {
            locale = "pt-BR";
        }
        return this.getMessage(id, Locale.forLanguageTag(locale));
    }

    public String getMessage(String id, Locale locale) {
        try {
            return messageSource.getMessage(id, null, locale);
        } catch (NoSuchMessageException ex) {
            return id;
        }
    }

    private ServiceResponse processMessage(ServiceResponse response) {
        String translated = this.getMessage(response.getMessage());
        return response.setMessage(translated);
    }

    private ServiceResponse processSignature(ServiceResponse response, String method, String httpVerb) {
        String app = "Enterative";
        String version = "v1.0.0";
        String signature = String.format("%s-%s@%s!%s", app, version, method, httpVerb);
        return response.setSignature(signature);
    }

    public ServiceResponse process(ServiceResponse response, String method, String httpVerb) {
        response = this.processMessage(response);
        response = this.processSignature(response, method, httpVerb);
        return response;
    }

    public String retrieveMerchantTerminalID(String shopCode, String terminalCode) {
        String s = String.format("%05d", Integer.parseInt(shopCode));
        String t = String.format("%03d", Integer.parseInt(terminalCode));
        return String.format("%s%s%s%s", s, "     ", t, "   ");
    }
}
