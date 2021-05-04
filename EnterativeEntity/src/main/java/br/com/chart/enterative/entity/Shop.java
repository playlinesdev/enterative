package br.com.chart.enterative.entity;

import br.com.chart.enterative.annotations.CEP;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

/**
 *
 * @author William Leite
 */
@Entity
@Table(name = "shop")
public class Shop extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 5)
    @Size(max = 5, message = ENTITY_MESSAGE.SIZE_5)
    @Getter @Setter private String code;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @Getter @Setter private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @Getter @Setter private Account account;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private STATUS status;

    @Column(name = "razao_social", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String razaoSocial;

    @Column(name = "fantasia", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String fantasia;

    @CNPJ(message = ENTITY_MESSAGE.INVALID_CNPJ)
    @Column(name = "cnpj", length = 14)
    @Size(max = 14, message = ENTITY_MESSAGE.SIZE_14)
    @Getter @Setter private String cnpj;
    
    @CPF(message = ENTITY_MESSAGE.INVALID_CPF)
    @Column(name = "cpf", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String cpf;

    @Column(name = "street", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String street;

    @Column(name = "street_number", length = 50)
    @Size(max = 50, message = ENTITY_MESSAGE.SIZE_50)
    @Getter @Setter private String number;

    @Column(name = "contact", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String contact;

    @Column(name = "district", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String district;

    @Column(name = "city", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String city;

    @Column(name = "country_state", length = 2)
    @Size(max = 2, message = ENTITY_MESSAGE.SIZE_2)
    @Getter @Setter private String state;

    @CEP(message = ENTITY_MESSAGE.INVALID_CEP)
    @Column(name = "cep", length = 8)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_8)
    @Getter @Setter private String cep;

    @Column(name = "payment_token", length = 36)
    @Size(max = 36, message = ENTITY_MESSAGE.SIZE_36)
    @Getter @Setter private String paymentToken;

    @Column(name = "country", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String country;

    @Column(name = "insc_estadual", length = 100)
    @Size(max = 100, message = ENTITY_MESSAGE.SIZE_100)
    @Getter @Setter private String inscEstadual;

    @Column(name = "insc_municipal", length = 100)
    @Size(max = 100, message = ENTITY_MESSAGE.SIZE_100)
    @Getter @Setter private String inscMunicipal;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter private List<ShopProductCommission> commissions;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter private List<ShopPhone> phones;
}
