package br.com.chart.enterative.repository;

import br.com.chart.enterative.dao.base.UserAwareRepository;
import br.com.chart.enterative.entity.AccountTransaction;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.enums.STATUS;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface UserRepository extends UserAwareRepository<User, Long>, JpaSpecificationExecutor<User> {

    public User findByLogin(String login);

    public User findByToken(String token);

    public List<User> findByShopCode(String code);

    public List<User> findByStatusOrderByName(STATUS status);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoriteProducts WHERE u.id = :id")
    public User findByIdWithFavoriteProductsEager(@Param("id") Long id);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE User SET token = :token WHERE id = :id")
    public void setTokenForID(@Param("token") String token, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User SET password = :password WHERE id = :id")
    public void setPasswordForID(@Param("password") String password, @Param("id") Long id);

    User findByForgotPasswordToken(String token);
}
