package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;

import br.com.chart.enterative.enums.STATUS;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class MerchantVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String name;
    @Getter @Setter private String cnpj;
    @Getter @Setter private String cpf;
    @Getter @Setter private String street;
    @Getter @Setter private String number;
    @Getter @Setter private String district;
    @Getter @Setter private String city;
    @Getter @Setter private String state;
    @Getter @Setter private String cep;
    @Getter @Setter private String phone;
    @Getter @Setter private String contact;
    @Getter @Setter private String email;
    @Getter @Setter private STATUS status;
    @Getter @Setter private String observation;
    @Getter @Setter private String acquiringInstitutionIdentifier;
    @Getter @Setter private String merchantIdentifier;
    @Getter @Setter private String merchantLocation;
    @Getter @Setter private MerchantCategoryVO category;
}
