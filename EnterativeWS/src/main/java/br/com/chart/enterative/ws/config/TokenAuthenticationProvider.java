package br.com.chart.enterative.ws.config;

import br.com.chart.enterative.dao.UserDAO;
import br.com.chart.enterative.entity.User;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Authentication Provider for login with token
 *
 * @author William Leite
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDAO userDAO;

    public boolean isValidCredentials(String username, String password) {
        if (Objects.nonNull(username) && Objects.nonNull(password) && !username.isEmpty() && !password.isEmpty()) {
            User user = this.userDAO.findByLogin(username);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordHash = encoder.encode(password);
            return passwordHash.equals(user.getPassword());
        }
        return false;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info("Iniciando autenticação por token");

        if (Objects.nonNull(username) && Objects.nonNull(password) && !username.isEmpty() && !password.isEmpty()) {
            User user = this.userDAO.findByLogin(username);
            if (Objects.nonNull(user) && Objects.nonNull(user.getToken()) && !user.getToken().isEmpty()) {
                if (Objects.equals(user.getToken(), password)) {
                    log.info("Usuário autenticado com sucesso!");
                    return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
                }
            }
        }

        log.info("Não foi possível autenticar o usuário por token");

        throw new BadCredentialsException("Não foi possível autenticar o usuário!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
