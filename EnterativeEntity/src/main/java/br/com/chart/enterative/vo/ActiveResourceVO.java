package br.com.chart.enterative.vo;

import br.com.chart.enterative.entity.Resource;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import static br.com.chart.enterative.enums.ACTIVATION_PROCESS.BHN;
import static br.com.chart.enterative.enums.ACTIVATION_PROCESS.EPAY;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ActiveResourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private Resource resource;
    @Getter @Setter private String authId;
    @Getter @Setter private Long terminalId;
    @Getter @Setter private Long retailerAcc;
    @Getter @Setter private LocalDateTime expirationDate;
    
    public boolean hasAuthId() {
        return Objects.nonNull(this.authId) && !this.authId.isEmpty();
    }

    public boolean isActive(ACTIVATION_PROCESS process) {
        boolean result;
        
        switch (process) {
            case EPAY:
                result = Objects.nonNull(this.expirationDate) && this.expirationDate.isAfter(LocalDateTime.now()) && this.hasAuthId();
                break;
            case BHN:
                result = Objects.nonNull(this.resource);
                break;
            default:
                result = false;
                break;
        }

        return result;
    }
}
