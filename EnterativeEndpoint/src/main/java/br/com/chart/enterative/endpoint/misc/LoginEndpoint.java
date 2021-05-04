package br.com.chart.enterative.endpoint.misc;

import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.vo.ServiceResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author William Leite
 */
@Controller
@RequestMapping("/login")
public class LoginEndpoint {

    @ResponseBody
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")    
    public ServiceResponse login(@RequestBody UserVO user, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Request-With");
        response.addHeader("Access-Control-Allow-Credentials", "true");

        return new ServiceResponse().setResponseCode(RESPONSE_CODE.E00);
    }
}
