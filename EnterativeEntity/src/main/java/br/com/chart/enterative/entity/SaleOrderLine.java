package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ACTIVATION_STATUS;
import br.com.chart.enterative.enums.CALLBACK_STATUS;
import br.com.chart.enterative.enums.EMAIL_STATUS;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.RESPONSE_CODE;
import br.com.chart.enterative.enums.SALE_ORDER_LINE_STATUS;
import br.com.chart.enterative.enums.SMS_STATUS;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "sale_order_line")
public class SaleOrderLine extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    @NotNull(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private SaleOrder saleOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private Product product;

    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date returnDate;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private SALE_ORDER_LINE_STATUS status;
    
    @Enumerated
    @Column(name = "user_email_status")
    @Getter @Setter private EMAIL_STATUS userEmailStatus;
    
    @Column(name = "user_email")
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String userEmail;
    
    @Enumerated
    @Column(name = "sms_status")
    @Getter @Setter private SMS_STATUS smsStatus;
    
    @Column(name = "user_cellphone")
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String userCellphone;

    @Column(name = "external_code", length = 20)
    @Size(max = 20, message = ENTITY_MESSAGE.SIZE_20)
    @NotBlank(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String externalCode;

    @Enumerated
    @Column(name = "response")
    @Getter @Setter private RESPONSE_CODE response;

    @Enumerated
    @Column(name = "response_aux")
    @Getter @Setter private RESPONSE_CODE responseAux;

    @Enumerated
    @Column(name = "activation_status")
    @Getter @Setter private ACTIVATION_STATUS activationStatus;

    @Enumerated
    @Column(name = "callback_status")
    @Getter @Setter private CALLBACK_STATUS callbackStatus;

    @Column(name = "barcode", length = 32)
    @Size(max = 32, message = ENTITY_MESSAGE.SIZE_32)
    @Getter @Setter private String barcode;

    @Column(name = "amount")
    @NumberFormat(pattern = "###,###,###.00")
    @Getter @Setter private BigDecimal amount;
    
    @Column(name = "operator")
    @Getter @Setter private String operator;
    
    @Column(name = "area_code")
    @Getter @Setter private String areaCode;
    
    @Column(name = "catalog_id")
    @Getter @Setter private String catalogId;
    
    @Column(name = "phone")
    @Getter @Setter private String phone;
    
    @Column(name = "product_type")
    @Getter @Setter private String productType;
}
