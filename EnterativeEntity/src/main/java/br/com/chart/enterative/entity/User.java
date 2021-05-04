package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import br.com.chart.enterative.enums.ENTITY_MESSAGE;
import br.com.chart.enterative.enums.STATUS;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity()
@Table(name = "web_user")
public class User extends UserAwareEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Column(name = "login", length = 255, unique = true)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @NotBlank(message = ENTITY_MESSAGE.REQUIRED)
    @Getter @Setter private String login;

    @Column(name = "terminal", length = 3)
    @Size(max = 3, message = ENTITY_MESSAGE.SIZE_3)
    @Getter @Setter private String terminal;

    @Column(name = "password", length = 255)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String password;

    @Column(name = "token", length = 255, unique = true)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String token;

    @Enumerated
    @Column(name = "status")
    @Getter @Setter private STATUS status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "web_user_web_user_role",
            joinColumns = @JoinColumn(table = "web_user", name = "web_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(table = "web_user_role", name = "web_user_role_id", referencedColumnName = "id"))
    @Getter @Setter private List<UserRole> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "web_user_product",
            joinColumns = @JoinColumn(table = "web_user", name = "web_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(table = "product", name = "product_id", referencedColumnName = "id"))
    @Getter @Setter private List<Product> favoriteProducts;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @Getter @Setter private Shop shop;

    @Email(message = ENTITY_MESSAGE.INVALID_EMAIL)
    @Column(name = "email", length = 255, unique = true)
    @Size(max = 255, message = ENTITY_MESSAGE.SIZE_255)
    @Getter @Setter private String email;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    @Getter @Setter private Partner partner;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @Getter @Setter private Account account;
    
    @Column(name = "locale")
    @Getter @Setter private String locale;
    
    @Column(name = "last_search_json")
    @Getter @Setter private String lastSearchJSON;

    @Column(name = "forgot_password_token")
    @Getter @Setter private String forgotPasswordToken;

    // User Details Overrides
    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus() == STATUS.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }
}
