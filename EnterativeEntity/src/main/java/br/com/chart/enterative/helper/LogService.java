package br.com.chart.enterative.helper;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author William Leite
 */
public abstract class LogService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void log(String message) {
        this.log.debug(message);
    }

    public void log(String message, Object... args) {
        this.log.debug(String.format(message, args));
    }

    public Locale locale() {
        return LocaleContextHolder.getLocale();
    }
}
