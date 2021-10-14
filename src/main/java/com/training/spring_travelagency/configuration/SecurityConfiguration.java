package com.training.spring_travelagency.configuration;

import com.training.spring_travelagency.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder passwordEncoder;

    SecurityConfiguration(UserService userService, AccessDeniedHandler accessDeniedHandler, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/","/index")
                .permitAll()
                .antMatchers("/users/**")
                .hasAuthority("ADMIN")
                .antMatchers("/registration", "/login")
                .anonymous()
                .anyRequest()
                .denyAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}