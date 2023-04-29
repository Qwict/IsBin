package com.qwict.isbin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(withDefaults());
        return http.build();
    }

    // Should worlk for custom fields but it doesn't
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .httpBasic(withDefaults())
//        		.exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint()))
//                .addFilterBefore(digestFilter());
//        return http.build();
//        return http.build();
//    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails owner = User.builder()
                .username("owner")
                .password(passwordEncoder().encode("owner"))
                .roles("ADMIN", "USER", "OWNER")
                .build();


        return new InMemoryUserDetailsManager(user, admin, owner);
    }
}
