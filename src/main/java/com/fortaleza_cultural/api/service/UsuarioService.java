package com.fortaleza_cultural.api.service;

import com.fortaleza_cultural.api.dto.UsuarioDto;
import com.fortaleza_cultural.api.model.Usuario;
import com.fortaleza_cultural.api.model.TipoUsuario;
import com.fortaleza_cultural.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.fortaleza_cultural.api.dto.LoginRequestDto; // Added import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * Validates if the email is already in use.
     * Hashes the password before saving.
     * Sets the creation timestamp and user type.
     *
     * @param usuarioDto Data Transfer Object containing user details (name, email, password, type).
     * @return Mono<Usuario> representing the saved user entity.
     *         Returns Mono.error with an IllegalArgumentException if email already exists or if user type is invalid.
     */
    public Mono<Usuario> registrarUsuario(UsuarioDto usuarioDto) {
        // 1. Check if email already exists
        return usuarioRepository.findByEmail(usuarioDto.getEmail())
            .flatMap(existingUser -> Mono.<Usuario>error(new IllegalArgumentException("Email já cadastrado: " + usuarioDto.getEmail())))
            .switchIfEmpty(Mono.defer(() -> {
                // 2. Create and populate new Usuario entity
                Usuario novoUsuario = new Usuario();
                novoUsuario.setNome(usuarioDto.getNome());
                novoUsuario.setEmail(usuarioDto.getEmail());
                // 3. Hash password
                novoUsuario.setSenhaHash(passwordEncoder.encode(usuarioDto.getSenha()));
                // 4. Set user type (convert from String to Enum)
                try {
                    novoUsuario.setTipo(TipoUsuario.valueOf(usuarioDto.getTipo().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    return Mono.error(new IllegalArgumentException("Tipo de usuário inválido: " + usuarioDto.getTipo()));
                }
                // 5. Set creation timestamp
                novoUsuario.setCriadoEm(LocalDateTime.now());

                // 6. Save the new user
                return usuarioRepository.save(novoUsuario);
            }));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return Mono<Usuario> representing the found user.
     *         Returns Mono.empty() if no user is found with the given ID.
     */
    public Mono<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Authenticates a user based on email and password.
     *
     * @param loginRequestDto DTO containing login credentials (email and password).
     * @return Mono<String> emitting a token (currently a placeholder) upon successful authentication.
     *         Returns Mono.error with an IllegalArgumentException if credentials are invalid
     *         or the user is not found.
     */
    public Mono<String> loginUsuario(LoginRequestDto loginRequestDto) {
        return usuarioRepository.findByEmail(loginRequestDto.getEmail())
            .flatMap(usuario -> {
                if (passwordEncoder.matches(loginRequestDto.getSenha(), usuario.getSenhaHash())) {
                    // Placeholder for token generation
                    // In a real application, generate a JWT or other secure token here.
                    String token = "fake-jwt-token:" + usuario.getEmail(); // Simple placeholder
                    return Mono.just(token);
                } else {
                    // Password mismatch
                    return Mono.error(new IllegalArgumentException("Credenciais inválidas. Senha não confere."));
                }
            })
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuário não encontrado com o email: " + loginRequestDto.getEmail())));
    }
}
