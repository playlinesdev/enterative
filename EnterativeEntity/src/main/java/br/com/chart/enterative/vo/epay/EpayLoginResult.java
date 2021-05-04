package br.com.chart.enterative.vo.epay;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author William Leite
 */
@XStreamAlias("LoginResult")
public class EpayLoginResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("AppName")
    @Getter @Setter private String appName;
    
    @XStreamAlias("AppProfile")
    @Getter @Setter private EpayAppProfile appProfile;
    
    // @Getter @Setter private EpayAppRoles appRoles;
    
    @XStreamAlias("AuthenticationId")
    @Getter @Setter private String authenticationId;
    
    @XStreamAlias("Expiration")
    @Getter @Setter private LocalDateTime expiration;
    
    @XStreamAlias("Username")
    @Getter @Setter private String username;
}
