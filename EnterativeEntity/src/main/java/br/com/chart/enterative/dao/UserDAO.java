package br.com.chart.enterative.dao;

import br.com.chart.enterative.dao.base.BaseRepository;
import br.com.chart.enterative.dao.base.UserAwareDAO;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.STATUS;
import br.com.chart.enterative.enums.USER_ROLE;
import br.com.chart.enterative.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 *
 * @author William Leite
 */
@Service
public class UserDAO extends UserAwareDAO<User, Long> {
    
    public UserDAO(BaseRepository<User, Long> repository) {
        super(repository);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public UserRepository repository() {
        return (UserRepository) super.repository();
    }
    
    public boolean hasRole(Long id, final USER_ROLE role) {
        User user = this.findOne(id);
        return this.hasRole(user, role);
    }
    
    public boolean hasRole(User user, final USER_ROLE role) {
        return user.getRoles().stream().anyMatch(r -> Objects.equals(r.getName(), role.getFullRole()));
    }
    
    public long count(Example<User> example) {
        return this.repository().count(example);
    }
    
    public User findByIdWithFavoriteProductsEager(Long id) {
        return this.repository().findByIdWithFavoriteProductsEager(id);
    }
    
    public User findByLogin(String login) {
        return this.repository().findByLogin(login);
    }
    
    public User findByToken(String token) {
        return this.repository().findByToken(token);
    }
    
    public List<User> findByShopCode(String code) {
        return this.repository().findByShopCode(code);
    }
    
    public List<User> findByStatusOrderByName(STATUS status) {
        return this.repository().findByStatusOrderByName(status);
    }
    
    public void setTokenForID(String token, Long id) {
        this.repository().setTokenForID(token, id);
    }
    
    public void setPasswordForID(String password, Long id) {
        this.repository().setPasswordForID(password, id);
    }

    public User findByForgotPasswordToken(String token) {
        return this.repository().findByForgotPasswordToken(token);
    }
}
