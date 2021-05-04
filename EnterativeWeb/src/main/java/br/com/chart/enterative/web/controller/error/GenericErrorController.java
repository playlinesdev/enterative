package br.com.chart.enterative.web.controller.error;

import br.com.chart.enterative.web.advices.GlobalControllerAdvice;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author William Leite
 */
@RestController
public class GenericErrorController implements ErrorController {

    @RequestMapping(value = GlobalControllerAdvice.DEFAULT_ERROR_VIEW)
    public String error() {
        return "Tratamento de Erro";
    }

    @Override
    public String getErrorPath() {
        return GlobalControllerAdvice.DEFAULT_ERROR_VIEW;
    }

}
