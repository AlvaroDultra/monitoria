package br.ucsal.monitoriaweb.config;

import br.ucsal.monitoriaweb.entity.Role;
import br.ucsal.monitoriaweb.entity.Usuario;
import br.ucsal.monitoriaweb.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar se já existe um admin
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setEmail("admin@ucsal.br");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setAtivo(true);
                usuarioRepository.save(admin);
                System.out.println("✅ Usuário ADMIN criado com sucesso!");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            }

            // Verificar se já existe um professor de teste
            if (usuarioRepository.findByUsername("professor").isEmpty()) {
                Usuario professor = new Usuario();
                professor.setUsername("professor");
                professor.setEmail("professor@ucsal.br");
                professor.setPassword(passwordEncoder.encode("professor123"));
                professor.setRole(Role.ROLE_PROFESSOR);
                professor.setAtivo(true);
                usuarioRepository.save(professor);
                System.out.println("✅ Usuário PROFESSOR criado com sucesso!");
                System.out.println("   Username: professor");
                System.out.println("   Password: professor123");
            }
        };
    }
}
