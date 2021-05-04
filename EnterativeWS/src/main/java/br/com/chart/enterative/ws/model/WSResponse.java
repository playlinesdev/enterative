package br.com.chart.enterative.ws.model;

import br.com.chart.enterative.vo.WSRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WSResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private T response;
    private String signature;

    private Integer currentPage;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;

    private WSResponse() {
    }

    public static <T> WSResponse<T> of(T response, String message, String method) {
        return WSResponse.of(response, method).setMessage(message);
    }

    public static <T> WSResponse<T> of(T response, String method) {
        WSResponse<T> result = new WSResponse<>();
        result.setResponse(response);
        return result.sign(method);
    }

    public static <T> WSResponse<T> of(T response, Integer currentPage, Integer totalPages, Long totalElements, Integer size) {
        WSResponse<T> result = new WSResponse<>();
        result.setCurrentPage(currentPage);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        result.setResponse(response);
        result.setSize(size);
        return result;
    }

    protected WSResponse<T> sign(String method) {
        String appName = "Enterative";
        String appVersion = "1.0.5";
        String signature = String.format("%s-v%s@%s", appName, appVersion, method);
        return this.setSignature(signature);
    }
}