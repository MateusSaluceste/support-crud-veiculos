package org.example.support.config;

import org.example.support.domain.entity.Usuario;
import org.example.support.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/login.html", "/css/**", "/js/**", "/h2-console/**",
                        "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/auth/me").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .headers(h -> h.frameOptions(f -> f.sameOrigin())); // para H2 console
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Usuario u = usuarioRepository.findByUsername(username)
                    .filter(Usuario::isAtivo)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            UserDetails details = User
                    .withUsername(u.getUsername())
                    .password(u.getPassword())
                    .roles(u.getRole().name())
                    .build();
            return details;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
