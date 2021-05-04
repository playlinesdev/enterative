package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ACTIVATION_PROCESS;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.PRODUCT_TYPE;
import br.com.chart.enterative.enums.STATUS;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ean", unique = true)
    @Size(max = 13, message = ENTITY_MESSAGE.SIZE_13)
    @NotBlank(message = ENTITY_MESSAGE.REQUIRED)
    private String ean;

    @Column(name = "display_name", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    private String displayName;

    @Column(name = "supplier_code", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    private String supplierCode;

    @Column(name = "supplier_ref_id", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    private String supplierRefId;

    @Column(name = "supplier_ref_category", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    private String supplierRefCategory;

    @Column(name = "supplier_ref_type", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    private String supplierRefType;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    private BigDecimal amount;

    @Column(name = "commission_percentage")
    @NumberFormat(pattern = "###.00")
    private BigDecimal commissionPercentage;

    @Column(name = "commission_amount")
    @NumberFormat(pattern = "###,###,###.00")
    private BigDecimal commissionAmount;

    @Column(name = "activation_fee")
    @NumberFormat(pattern = "###,###,###.00")
    private BigDecimal activationFee;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "terms_and_conditions")
    private ProductText termsAndConditions;

    @ManyToOne
    @JoinColumn(name = "activation_instructions")
    private ProductText activationInstructions;

    @Enumerated
    @Column(name = "status")
    private STATUS status;

    @Enumerated
    @Column(name = "type")
    private PRODUCT_TYPE type;

    @Enumerated
    @Column(name = "activation_process")
    private ACTIVATION_PROCESS activationProcess;

    @Column(name = "primary_account_number", length = 20)
    @Size(message = ENTITY_MESSAGE.SIZE_20)
    private String primaryAccountNumber;

    @Column(name = "sends_sms")
    private Boolean sendsSMS;

    @Column(name = "sms_template", length = 255)
    @Size(message = ENTITY_MESSAGE.SIZE_255)
    private String smsTemplate;
}
