package br.com.chart.enterative.vo;

import br.com.chart.enterative.enums.RESPONSE_CODE;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

/**
 *
 * @author William Leite
 */
public class ServiceResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter private String message;
    @Getter private RESPONSE_CODE responseCode;
    @Getter private Object response;
    @Getter private String signature;

    public ServiceResponse handleException(Exception e) {
        this.setResponseCode(RESPONSE_CODE.E99);
        if (Objects.nonNull(e.getMessage()) && !e.getMessage().isEmpty()) {
            this.setMessage(e.getMessage());
        } else {
            this.setMessage(e.toString());
        }
        return this;
    }

    public ServiceResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceResponse setMessage(String message, Object... args) {
        this.message = String.format(message, args);
        return this;
    }

    public ServiceResponse setResponseCode(RESPONSE_CODE responseCode) {
        this.responseCode = responseCode;
        if (Objects.isNull(this.message) || this.message.isEmpty()) {
            this.setMessage(responseCode.getDescription());
        }
        return this;
    }

    public ServiceResponse setResponse(Object response) {
        this.response = response;
        return this;
    }

    public ServiceResponse setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> ServiceResponse put(String key, T value) {
        Map<String, T> map;
        if (Objects.nonNull(this.response) && this.response instanceof Map) {
            map = (Map) this.response;
        } else {
            map = new HashMap<>();
        }

        map.put(key, value);
        this.response = map;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (Objects.nonNull(this.response) && this.response instanceof Map) {
            return ((Map<String, T>) this.response).get(key);
        }
        return null;
    }
}
