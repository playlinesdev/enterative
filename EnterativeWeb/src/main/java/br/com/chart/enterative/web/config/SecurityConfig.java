package br.com.chart.enterative.web.config;

import br.com.chart.enterative.enums.USER_ROLE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Desabilita CSRF para chamada de callback
        http.csrf().ignoringAntMatchers("/callback**");

        http.authorizeRequests()
                .antMatchers("/ativacao/fisico/bhn").hasRole(USER_ROLE.ROLE_BHN.getRole())
                .antMatchers("/ativacao/fisico/balance").hasRole(USER_ROLE.ROLE_ENTERATIVE_BALANCE.getRole())
                .antMatchers("/ativacao/fisico/fast").hasRole(USER_ROLE.ROLE_FAST_ACTIVATION.getRole())
                .antMatchers("/ativacao/**").authenticated()
                .antMatchers("/user/new/**").permitAll()
                .antMatchers("/user/email/**").permitAll()
                .antMatchers("/user/forgotpassword/**").permitAll()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/product/**").hasAnyRole(USER_ROLE.ROLE_CHART_SUPPORT.getRole(), USER_ROLE.ROLE_ADMIN.getRole())
                .antMatchers("/admin/productcategory/**").hasAnyRole(USER_ROLE.ROLE_CHART_SUPPORT.getRole(), USER_ROLE.ROLE_ADMIN.getRole())
                .antMatchers("/admin/**").hasRole(USER_ROLE.ROLE_ADMIN.getRole())
                .antMatchers("/account/excerpt/**").hasAnyRole(USER_ROLE.ROLE_ADMIN.getRole(), USER_ROLE.ROLE_SHOP_ADMIN.getRole(), USER_ROLE.ROLE_CUSTOMER.getRole(), USER_ROLE.ROLE_CHART_SUPPORT.getRole())
                .antMatchers("/account/transaction/**").hasAnyRole(USER_ROLE.ROLE_ADMIN.getRole(), USER_ROLE.ROLE_SHOP_ADMIN.getRole(), USER_ROLE.ROLE_CUSTOMER.getRole(), USER_ROLE.ROLE_CHART_SUPPORT.getRole())
                .antMatchers("/account/**").hasAnyRole(USER_ROLE.ROLE_ADMIN.getRole(), USER_ROLE.ROLE_SHOP_ADMIN.getRole(), USER_ROLE.ROLE_CUSTOMER.getRole())
                .antMatchers("/shop/purchaseorder/**").hasAnyRole(USER_ROLE.ROLE_ADMIN.getRole(), USER_ROLE.ROLE_SHOP_ADMIN.getRole(), USER_ROLE.ROLE_CHART_SUPPORT.getRole())
                .antMatchers("/shop/**").hasAnyRole(USER_ROLE.ROLE_ADMIN.getRole(), USER_ROLE.ROLE_SHOP_ADMIN.getRole())
                .antMatchers("/store/**").authenticated()
                .antMatchers("/cart/**").authenticated()
                .antMatchers("/**").permitAll()
                .and().formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/loginsuccess")
                .and().logout().permitAll();
//		.and().exceptionHandling().accessDeniedPage("/accessDenied.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

}
