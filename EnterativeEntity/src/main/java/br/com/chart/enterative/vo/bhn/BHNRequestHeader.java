package br.com.chart.enterative.vo.bhn;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BHNRequestHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BHNRequestDetails details;
    @Getter @Setter private String signature;

    public BHNRequestHeader() {
        this.details = new BHNRequestDetails();
    }

}
