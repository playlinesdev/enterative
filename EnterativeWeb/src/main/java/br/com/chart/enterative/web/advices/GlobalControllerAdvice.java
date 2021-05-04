package br.com.chart.enterative.web.advices;

import br.com.chart.enterative.helper.EnterativeUtils;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerAdvice {

    public static final String DEFAULT_ERROR_VIEW = "genericError";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mv = new ModelAndView(DEFAULT_ERROR_VIEW);
        mv.addObject("maintenance", EnterativeUtils.MAINTENANCE);
        mv.addObject("exception", e);
        mv.addObject("url", req.getRequestURL());
        e.printStackTrace();
        return mv;
    }

}
