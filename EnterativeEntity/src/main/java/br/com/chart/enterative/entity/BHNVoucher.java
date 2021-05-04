package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bhn_voucher")
public class BHNVoucher extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "ean", length = 13)
    @Size(max = 13, message = ENTITY_MESSAGE.SIZE_13)
    @Getter @Setter private String ean;

    @Column(name = "card_number", length = 19)
    @Size(max = 19, message = ENTITY_MESSAGE.SIZE_19)
    @Getter @Setter private String cardNumber;

    @Column(name = "amount", length = 12)
    @Size(max = 12, message = ENTITY_MESSAGE.SIZE_12)
    @Getter @Setter private String amount;

    @Column(name = "pin", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String pin;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @Getter @Setter private Product product;
}
