package com.training.spring_travelagency.configuration;

import com.training.spring_travelagency.handler.LoggingAccessDeniedHandler;
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
    private final LoggingAccessDeniedHandler loggingAccessDeniedHandler;
    private final PasswordEncoder passwordEncoder;

    SecurityConfiguration(UserService userService, AccessDeniedHandler accessDeniedHandler, LoggingAccessDeniedHandler loggingAccessDeniedHandler, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.loggingAccessDeniedHandler = loggingAccessDeniedHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/tours","/404", "/500", "/403", "/access-denied")
                .permitAll()
                .antMatchers("/user-cabinet",  "/orders/add/**", "/orders/delete/**")
                .hasAnyAuthority("ADMIN", "MANAGER", "USER")
                .antMatchers("/tours/toggle-hot/**", "/orders", "/orders/set-confirmed/**", "/orders/set-rejected/**", "/orders/set-discount/**")
                .hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/users/**", "/tours/add", "/tours/update/**","/tours/delete/**")
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
                .logoutSuccessUrl("/login")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(loggingAccessDeniedHandler)
                .accessDeniedPage("/403");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}