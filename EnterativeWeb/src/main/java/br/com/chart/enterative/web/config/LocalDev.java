package br.com.chart.enterative.web.config;

import br.com.chart.enterative.entity.EnvParameter;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.repository.EnvParameterRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LocalDev {
    private final EnvParameterRepository repository;

    public LocalDev(EnvParameterRepository repository) {
        this.repository = repository;
    }

    public void initLocalDev() {
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CHECK_PAYMENT_CIELO_URL, "https://localhost:8125/checkout/checkpay/cielo/");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CHECK_PAYMENT_PAGSEGURO_URL, "https://localhost:8125/checkout/checkpay/pagseguro/");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CIELO_CART_REDIRECT_URL, "https://localhost:8443/enterative/cart/pay/done/cielo");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_CIELO_REDIRECT_URL, "https://localhost:8443/enterative/ativacao/fisico/customer/payment/done/cielo");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ENTERATIVE_CARD_REDIRECT_URL, "https://localhost:8443/enterative/ativacao/fisico/customer/payment/done/enterative");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ENTERATIVE_CART_REDIRECT_URL, "https://localhost:8443/enterative/cart/pay/done/enterative");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_INIT_PAYMENT_URL, "https://localhost:8125/checkout/init/");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_ORDER_URL, "https://localhost:8125/order");
        this.setParam(ENVIRONMENT_PARAMETER.FILE_SDF_SFTP_URL, "");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_METHOD_URL, "https://localhost:8125/paymentmethod/list/");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_PAGSEGURO_CART_REDIRECT_URL, "https://localhost:8443/enterative/cart/pay/done/pagseguro");
        this.setParam(ENVIRONMENT_PARAMETER.PAYMENT_MANAGER_PAGSEGURO_REDIRECT_URL, "https://localhost:8443/enterative/ativacao/fisico/customer/payment/done/pagseguro");
        this.setParam(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_CALLBACKURL, "https://localhost:8443/enterative/callback");
        this.setParam(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_DOMAIN, "localhost");
        this.setParam(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_PATH, "https://localhost:8123/ws/ativacao");
        this.setParam(ENVIRONMENT_PARAMETER.WEB_WSENTERATIVE_STATUS_PATH, "https://localhost:8123/ws/status");
        this.setParam(ENVIRONMENT_PARAMETER.ENTERATIVE_JKS, "C:\\Projects\\Enterative\\Project\\EnterativeWS\\src\\main\\resources\\enterative.p12");
    }

    private void setParam(ENVIRONMENT_PARAMETER param, String value) {
        EnvParameter parameter = this.repository.findByParam(param);
        if (Objects.isNull(parameter)) {
            parameter = new EnvParameter();
            parameter.setParam(param);
        }
        parameter.setValue(value);
        this.repository.saveAndFlush(parameter);
    }
}
