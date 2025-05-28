package com.fortaleza_cultural.api.repository;

import com.fortaleza_cultural.api.model.Usuario;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for {@link Usuario} entities.
 * Provides CRUD operations and custom query methods.
 */
public interface UsuarioRepository extends R2dbcRepository<Usuario, Long> {

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for.
     * @return a {@link Mono} emitting the found {@link Usuario} or {@link Mono#empty()} if not found.
     */
    Mono<Usuario> findByEmail(String email);
}
