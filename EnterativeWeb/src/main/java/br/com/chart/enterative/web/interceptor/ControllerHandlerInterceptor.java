package br.com.chart.enterative.web.interceptor;

import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.helper.EnterativeUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author William Leite
 */
@Component
public class ControllerHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private EnvParameterDAO parameterDAO;

    public Date toDate(String pattern, String date) throws ParseException {
        return new SimpleDateFormat(pattern).parse(date);
    }

    public String fromDate(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String maintenanceTime = this.parameterDAO.get(ENVIRONMENT_PARAMETER.MAINTENANCE);
        if (Objects.nonNull(maintenanceTime) && !maintenanceTime.isEmpty()) {
            Date date = this.toDate("ddMMyyyy HHmmss", maintenanceTime);
            if (date.after(new Date())) {
                EnterativeUtils.MAINTENANCE = String.format("Sistema entrará em manutenção em [%s]!", this.fromDate("dd/MM/yyyy HH:mm:ss", date));
            } else {
                EnterativeUtils.MAINTENANCE = null;
            }
        } else {
            EnterativeUtils.MAINTENANCE = null;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, ModelAndView mav) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest hsr, HttpServletResponse hsr1, Object o, Exception excptn) throws Exception {
    }

}
