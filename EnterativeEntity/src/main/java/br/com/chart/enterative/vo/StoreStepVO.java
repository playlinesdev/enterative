package br.com.chart.enterative.vo;

import br.com.chart.enterative.vo.epay.EpayCatalog;
import br.com.chart.enterative.vo.epay.EpayDisplayCatalog;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class StoreStepVO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Getter @Setter private EpayDisplayCatalog displayCatalog;
    @Getter @Setter private String areaCode;
    @Getter @Setter private EpayCatalog product;
    @Getter @Setter private String productJson;
    @Getter @Setter private String phone;
    @Getter @Setter private Long currentStep;
    @Getter @Setter private Long totalSteps;
    @Getter @Setter private String contractCPF;
    
    public StoreStepVO() {
    }
    
    public StoreStepVO(Long totalSteps) {
        this.currentStep = 1L;
        this.totalSteps = totalSteps;
    }
}
