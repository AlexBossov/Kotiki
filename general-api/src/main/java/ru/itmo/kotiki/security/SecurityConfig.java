package ru.itmo.kotiki.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.itmo.kotiki.filter.AuthenticationFilter;
import ru.itmo.kotiki.filter.AuthorizationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**", "/api/owners/**").permitAll();

//        http.authorizeRequests().antMatchers(GET, "/api/owners/**").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(DELETE, "/api/owners/**").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(PUT, "/api/owners/**").hasAnyAuthority("ROLE_USER");
//        http.authorizeRequests().antMatchers(POST, "/api/owners/**").hasAnyAuthority("ROLE_USER");

        http.authorizeRequests().antMatchers(GET, "/api/cats/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/cats/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/api/cats/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/api/cats/**").hasAnyAuthority("ROLE_USER");

        http.authorizeRequests().antMatchers(POST, "/token/refresh").hasAnyAuthority("ROLE_USER");

        http.authorizeRequests().antMatchers(PUT, "/api/users/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/users/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/roles/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

