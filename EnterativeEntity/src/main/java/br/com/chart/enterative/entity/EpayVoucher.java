package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "epay_voucher")
public class EpayVoucher extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ean", length = 13)
    @Size(max = 13, message = ENTITY_MESSAGE.SIZE_13)
    @Getter @Setter private String ean;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter @Setter private Product product;
    
    @Column(name = "operator", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String operator;
    
    @Column(name = "area_code", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String areaCode;
    
    @Column(name = "phone", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String phone;
    
    @Column(name = "epay_product_id")
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String epayProductId;
    
    @Column(name = "amount")
    @Getter @Setter private BigDecimal amount;
}
