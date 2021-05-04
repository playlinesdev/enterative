package br.com.chart.enterative.entity.vo;

import br.com.chart.enterative.entity.vo.base.UserAwareVO;
import br.com.chart.enterative.enums.STATUS;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
public class UserVO extends UserAwareVO {

    private static final long serialVersionUID = 1L;

    @Getter @Setter private String login;
    @Getter @Setter private String terminal;
    @Getter @Setter private String password;
    @Getter @Setter private String token;
    @Getter @Setter private STATUS status;
    @Getter @Setter private List<UserRoleVO> roles;
    @Getter @Setter private ShopVO shop;
    @Getter @Setter private AccountVO account;
    @Getter @Setter private String email;
    @Getter @Setter private PartnerVO partner;
    @Getter @Setter private String locale;
    // UI
    @Getter @Setter private String passwordMatch;
    @Getter @Setter private String emailMatch;
    @Getter @Setter private String lastSearchJSON;
}
