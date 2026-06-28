package com.example.ms_usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // ✅ PÚBLICO
                .requestMatchers("/api/public/**").permitAll()

                // ✅ 🔥 ACTUATOR (CLAVE PARA ARREGLAR TU ERROR)
                .requestMatchers("/actuator/**").permitAll()

                // ✅ V1
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/usuarios/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/empleados/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/permisos/**").hasRole("ADMIN")

                // ✅ V2
                .requestMatchers("/api/v2/usuarios/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/v2/empleados/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/v2/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/v2/permisos/**").hasRole("ADMIN")

                // ✅ TODO LO DEMÁS REQUIERE LOGIN
                
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        UserDetails user = User
            .withUsername("user")
            .password(encoder.encode("1234"))
            .roles("USER")
            .build();

        UserDetails admin = User
            .withUsername("admin")
            .password(encoder.encode("1234"))
            .roles("ADMIN", "USER")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}