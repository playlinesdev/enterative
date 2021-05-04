package br.com.chart.enterative.entity;

import br.com.chart.enterative.annotations.CEP;
import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import br.com.chart.enterative.enums.STATUS;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "merchant")
public class Merchant extends UserAwareEntity {

    private static final long serialVersionUID = 1L;

    @CNPJ(message = ENTITY_MESSAGE.INVALID_CNPJ)
    @Column(name = "cnpj", length = 14)
    @Size(max = 14, message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String cnpj;

    @CPF(message = ENTITY_MESSAGE.INVALID_CPF)
    @Column(name = "cpf", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String cpf;

    @Column(name = "street", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String street;

    @Column(name = "street_number", length = 50)
    @Size(max = 50, message = ENTITY_MESSAGE.SIZE_50)
    @Getter @Setter private String number;

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
    @Size(max = 8, message = ENTITY_MESSAGE.SIZE_8)
    @Getter @Setter private String cep;

    @Column(name = "phone", length = 11)
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String phone;

    @Column(name = "contact", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String contact;

    @Email(message = ENTITY_MESSAGE.INVALID_EMAIL)
    @Column(name = "email", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String email;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private STATUS status;

    @Column(name = "observation", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String observation;

    @Column(length = 11, name = "acquiring_institution_identifier")
    @Size(max = 11, message = ENTITY_MESSAGE.SIZE_11)
    @Getter @Setter private String acquiringInstitutionIdentifier;

    @Column(length = 15, name = "merchant_identifier")
    @Size(max = 15, message = ENTITY_MESSAGE.SIZE_15)
    @Getter @Setter private String merchantIdentifier;

    @Column(length = 40, name = "merchant_location")
    @Size(max = 40, message = ENTITY_MESSAGE.SIZE_40)
    @Getter @Setter private String merchantLocation;

    @ManyToOne
    @JoinColumn(name = "merchant_category_id")
    @Getter @Setter private MerchantCategory category;
}
