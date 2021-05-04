package br.com.chart.enterative.ws.config;

import br.com.chart.enterative.enums.USER_ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SegurancaConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/ping").permitAll()
                .antMatchers("/teste*/**").permitAll()
                .antMatchers("/flutter/**").authenticated()
                .antMatchers("/ws/**").hasAnyRole(
                USER_ROLE.ROLE_BHN.getRole(),
                USER_ROLE.ROLE_ENTERATIVE_BALANCE.getRole(),
                USER_ROLE.ROLE_ENTERATIVE_CREDIT.getRole(),
                USER_ROLE.ROLE_CUSTOMER.getRole(),
                USER_ROLE.ROLE_FAST_ACTIVATION.getRole())
                .antMatchers("/paygoIntegrator/**").hasRole(USER_ROLE.ROLE_PAYGO_INTEGRATOR.getRole())
                .and().httpBasic();

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider)
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
