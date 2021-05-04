package br.com.chart.enterative.service.base;

import br.com.chart.enterative.helper.LogService;
import br.com.chart.enterative.dao.EnvParameterDAO;
import br.com.chart.enterative.dao.UserDAO;
import br.com.chart.enterative.entity.Account;
import br.com.chart.enterative.entity.User;
import br.com.chart.enterative.entity.vo.AccountVO;
import br.com.chart.enterative.entity.vo.UserVO;
import br.com.chart.enterative.enums.ENVIRONMENT_PARAMETER;
import br.com.chart.enterative.enums.USER_ROLE;
import br.com.chart.enterative.converter.UserConverterService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author William Leite
 */
public abstract class UserAwareComponent extends LogService {

    @Autowired
    protected EnvParameterDAO parameterDAO;

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    private UserConverterService userConverter;

    public Long retrieveAccountID(User user, Long searchAccountID) {
        Long accountID = null;
        if (this.userDAO.hasRole(user, USER_ROLE.ROLE_ADMIN) && Objects.nonNull(searchAccountID)) {
            accountID = searchAccountID;
        } else if (this.userDAO.hasRole(user, USER_ROLE.ROLE_SHOP_ADMIN)) {
            accountID = user.getAccount().getId();
        }
        return accountID;
    }

    public Long retrieveAccountID(User user, AccountVO searchAccount) {
        Long id = Objects.nonNull(searchAccount) ? searchAccount.getId() : null;
        return this.retrieveAccountID(user, id);
    }

    public Long retrieveAccountID(User user, Account searchAccount) {
        Long id = Objects.nonNull(searchAccount) ? searchAccount.getId() : null;
        return this.retrieveAccountID(user, id);
    }

    protected User systemUser() {
        return this.userDAO.findOne(this.systemUserId());
    }

    protected Long systemUserId() {
        return this.parameterDAO.get(ENVIRONMENT_PARAMETER.SYSTEM_USER);
    }

    protected Long loggedUserId() {
        Long id = null;
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getPrincipal() instanceof User) {
                id = ((User) auth.getPrincipal()).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }
        return id;
    }

    protected User loggedUser() {
        User user = null;
        try {
            Long userID = this.loggedUserId();
            if (Objects.nonNull(userID)) {
                user = this.userDAO.findOne(userID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.log(e.getMessage());
        }
        return user;
    }

    protected UserVO loggedUserVO() {
        User user = this.loggedUser();
        return this.userConverter.convert(user);
    }
}
