package org.example.support.config;

import org.example.support.domain.entity.Usuario;
import org.example.support.domain.enums.Role;
import org.example.support.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedUsers(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // admin
            usuarioRepository.findByUsername("admin").orElseGet(() -> {
                Usuario u = new Usuario();
                u.setUsername("admin");
                u.setPassword(passwordEncoder.encode("admin123"));
                u.setRole(Role.ADMIN);
                u.setAtivo(true);
                return usuarioRepository.save(u);
            });

            // vendedor
            usuarioRepository.findByUsername("vendedor").orElseGet(() -> {
                Usuario u = new Usuario();
                u.setUsername("vendedor");
                u.setPassword(passwordEncoder.encode("vendedor123"));
                u.setRole(Role.VENDEDOR);
                u.setAtivo(true);
                return usuarioRepository.save(u);
            });
        };
    }
}
