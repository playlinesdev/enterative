package br.com.chart.enterative.entity;

import br.com.chart.enterative.entity.base.UserAwareEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "web_user_role")
public class UserRole extends UserAwareEntity implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
