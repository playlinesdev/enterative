package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class ProductVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String ean;
    @Getter @Setter private String displayName;
    @Getter @Setter private String supplierCode;
    @Getter @Setter private String imagem;
    @Getter @Setter private String supplierRefId;
    @Getter @Setter private String supplierRefCategory;
    @Getter @Setter private String supplierRefType;
    @Getter @Setter private BigDecimal amount;
    @Getter @Setter private BigDecimal commissionPercentage;
    @Getter @Setter private BigDecimal commissionAmount;
    @Getter @Setter private BigDecimal activationFee;
    @Getter @Setter private SupplierVO supplier;
    @Getter @Setter private ProductCategoryVO category;
    @Getter @Setter private STATUS status;
    @Getter @Setter private Boolean favorite;
    @Getter @Setter private Boolean available;
    @Getter @Setter private PRODUCT_TYPE type;
    @Getter @Setter private ProductTextVO termsAndConditions;
    @Getter @Setter private ProductTextVO activationInstructions;
    @Getter @Setter private ACTIVATION_PROCESS activationProcess;
    @Getter @Setter private String primaryAccountNumber;
    @Getter @Setter private Boolean sendsSMS;
    @Getter @Setter private String smsTemplate;
    @Getter @Setter private BigDecimal propagateCommission;

    public ProductVO() {
    }

    public ProductVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
