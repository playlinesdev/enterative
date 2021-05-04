package br.com.chart.enterative.vo.bhn;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BHNRequestContainer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private BHNRequestHeader header;
    @Getter @Setter private BHNRequestTransaction transaction;

    public BHNRequestContainer() {
        this.header = new BHNRequestHeader();
        this.transaction = new BHNRequestTransaction();
    }
}
