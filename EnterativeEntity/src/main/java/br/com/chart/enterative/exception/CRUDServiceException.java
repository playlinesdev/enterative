package br.com.chart.enterative.exception;

import br.com.chart.enterative.vo.ServiceResponse;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class CRUDServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private ServiceResponse response;
    @Getter @Setter private boolean translated;

    public CRUDServiceException() {
        this.response = null;
        this.translated = false;
    }

    public CRUDServiceException(ServiceResponse response) {
        super(response.getMessage());
        this.response = response;
        this.translated = false;
    }

    public CRUDServiceException(ServiceResponse response, boolean translated) {
        super(response.getMessage());
        this.response = response;
        this.translated = translated;
    }
}
