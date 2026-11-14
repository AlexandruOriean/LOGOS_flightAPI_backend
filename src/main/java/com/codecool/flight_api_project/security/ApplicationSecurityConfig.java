package com.codecool.flight_api_project.security;

import com.codecool.flight_api_project.security.jwt.AuthEntryPointJwt;
import com.codecool.flight_api_project.security.jwt.AuthTokenFilter;
import com.codecool.flight_api_project.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
                .frameOptions()
                .disable()
                .and()
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/**")
                .permitAll()
                .antMatchers("/h2-console/**", "/swagger-ui/#/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/flights/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/flights")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/flights")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/flights/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/flights/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/v1/cities")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/cities")
                .authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/cities/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/cities/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/airports")
                .authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/airports/{id}")
                .authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/airports/iata/{iata_code}")
                .authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/airports")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/airports/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/airports/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/airplanes")
                .authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/airplanes")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/airplanes/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/airplanes/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/airlines")
                .authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/airlines")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/airlines/{id}")
                .hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/v1/airlines/{id}")
                .hasRole("ADMIN")
                .anyRequest()
                .permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
