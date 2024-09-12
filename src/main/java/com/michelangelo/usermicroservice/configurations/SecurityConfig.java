/*
package com.michelangelo.usermicroservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf((csrf->csrf.disable()))
                .authorizeHttpRequests((authorize)->authorize
.requestMatchers(
                                "/v2/**")
                        .hasRole("USER")

                        //.requestMatchers(//"/api/v5/availability",
                        //"/api/v5/booking",
                        //"/api/v5/mybookings",
                        //"/v1/media/**")
                        //.hasRole("USER")
                        .anyRequest()
                        .authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails userDetails1 = User.withDefaultPasswordEncoder()
                .username("Kalle")
                .password("Kalle")
                .roles("USER")
                .build();
        UserDetails userDetails2 = User.withDefaultPasswordEncoder()
                .username("John")
                .password("John")
                .roles("USER")
                .build();
        UserDetails userDetails3 = User.withDefaultPasswordEncoder()
                .username("Sara")
                .password("Sara")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails1,userDetails2,userDetails3);
    }
}
*/
