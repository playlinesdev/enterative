package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.ShopProductCommissionTemplate;
import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William
 */
public class ShopVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String code;
    @Getter @Setter private MerchantVO merchant;
    @Getter @Setter private STATUS status;
    @Getter @Setter private String razaoSocial;
    @Getter @Setter private String fantasia;
    @Getter @Setter private String cnpj;
    @Getter @Setter private String cpf;
    @Getter @Setter private String street;
    @Getter @Setter private String number;
    @Getter @Setter private String contact;
    @Getter @Setter private String district;
    @Getter @Setter private String city;
    @Getter @Setter private String state;
    @Getter @Setter private String cep;
    @Getter @Setter private String country;
    @Getter @Setter private String inscEstadual;
    @Getter @Setter private String inscMunicipal;
    @Getter @Setter private AccountVO account;
    @Getter @Setter private List<ShopProductCommissionVO> commissions;
    @Getter @Setter private List<ShopPhoneVO> phones;
    @Getter @Setter private ShopProductCommissionTemplate template;
    @Getter @Setter private ShopVO parent;
}
