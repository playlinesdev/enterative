package br.com.chart.enterative.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *
 * @author William Leite
 */
@Component
public class EnterativeLocaleChangeInterceptor extends LocaleChangeInterceptor {

    public EnterativeLocaleChangeInterceptor() {
        this.setParamName("lang");
    }
}
