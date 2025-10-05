package com.wecp.progressive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wecp.progressive.jwt.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter =jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // .antMatchers("/users/register", "/users/login").permitAll()
                .antMatchers(HttpMethod.GET,"/accounts/user/{userId}").hasAuthority("USER")
                .antMatchers(HttpMethod.POST, "/transactions").hasAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/customers/{customerId}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.GET,"/transactions/user/{customerId}").hasAnyAuthority("USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/customers").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/accounts/{accountId}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/accounts/{accountId}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/customers/{customerId}").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}