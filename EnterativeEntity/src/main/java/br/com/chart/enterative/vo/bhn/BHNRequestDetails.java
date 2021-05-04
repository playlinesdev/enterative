package br.com.chart.enterative.vo.bhn;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BHNRequestDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String productCategoryCode;
    @Getter @Setter private String specVersion;
    @Getter @Setter private String statusCode;
}
