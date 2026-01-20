package com.farmacia.system.config;

import com.farmacia.system.entity.Categoria;
import com.farmacia.system.entity.Medicamento;
import com.farmacia.system.entity.Role;
import com.farmacia.system.entity.Usuario;
import com.farmacia.system.repository.CategoriaRepository;
import com.farmacia.system.repository.MedicamentoRepository;
import com.farmacia.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedCategoriasAndMedicamentos();
    }

    private void seedUsers() {
        // Garantir que o admin existe, mesmo se já houver outros usuários
        usuarioRepository.findByEmail("admin@farmacia.com").ifPresentOrElse(
                admin -> {
                    // Se o admin existe, garantir que a senha está correta (hasheada)
                    // Se a senha não começar com $2a$ (formato BCrypt), atualizar
                    if (!admin.getSenha().startsWith("$2a$")) {
                        admin.setSenha(passwordEncoder.encode("admin123"));
                        usuarioRepository.save(admin);
                        System.out.println("Admin password updated: admin@farmacia.com / admin123");
                    }
                },
                () -> {
                    // Criar admin se não existir
                    Usuario admin = Usuario.builder()
                            .nome("Administrador")
                            .email("admin@farmacia.com")
                            .senha(passwordEncoder.encode("admin123"))
                            .role(Role.ADMIN)
                            .build();
                    usuarioRepository.save(admin);
                    System.out.println("Admin user created: admin@farmacia.com / admin123");
                }
        );

        // Garantir que o vendedor existe
        usuarioRepository.findByEmail("vendedor@farmacia.com").ifPresentOrElse(
                vendedor -> {
                    // Se o vendedor existe, garantir que a senha está correta (hasheada)
                    if (!vendedor.getSenha().startsWith("$2a$")) {
                        vendedor.setSenha(passwordEncoder.encode("vendedor123"));
                        usuarioRepository.save(vendedor);
                        System.out.println("Vendedor password updated: vendedor@farmacia.com / vendedor123");
                    }
                },
                () -> {
                    // Criar vendedor se não existir
                    Usuario vendedor = Usuario.builder()
                            .nome("Vendedor")
                            .email("vendedor@farmacia.com")
                            .senha(passwordEncoder.encode("vendedor123"))
                            .role(Role.VENDEDOR)
                            .build();
                    usuarioRepository.save(vendedor);
                    System.out.println("Vendedor user created: vendedor@farmacia.com / vendedor123");
                }
        );

        System.out.println("Users verified: admin@farmacia.com / vendedor@farmacia.com");
    }

    private void seedCategoriasAndMedicamentos() {
        if (categoriaRepository.count() == 0) {
            Categoria analgesicos = new Categoria();
            analgesicos.setNome("Analgésicos");
            categoriaRepository.save(analgesicos);

            Categoria antibioticos = new Categoria();
            antibioticos.setNome("Antibióticos");
            categoriaRepository.save(antibioticos);

            if (medicamentoRepository.count() == 0) {
                Medicamento med1 = Medicamento.builder()
                        .nome("Paracetamol 500mg")
                        .preco(new BigDecimal("15.50"))
                        .estoque(100)
                        .validade(LocalDate.now().plusYears(2))
                        .ativo(true)
                        .categoria(analgesicos)
                        .build();
                medicamentoRepository.save(med1);

                Medicamento med2 = Medicamento.builder()
                        .nome("Amoxicilina 875mg")
                        .preco(new BigDecimal("45.90"))
                        .estoque(50)
                        .validade(LocalDate.now().plusMonths(6))
                        .ativo(true)
                        .categoria(antibioticos)
                        .build();
                medicamentoRepository.save(med2);

                System.out.println("Products Seeded");
            }
        }
    }
}
