package com.fortaleza_cultural.api.controller;

import com.fortaleza_cultural.api.dto.UsuarioDto;
import com.fortaleza_cultural.api.model.Usuario;
import com.fortaleza_cultural.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for handling user-related HTTP requests.
 * Exposes endpoints for user registration and retrieval.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Constructs a new UsuarioController with the given UsuarioService.
     *
     * @param usuarioService The service to handle user business logic.
     */
    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint for registering a new user.
     *
     * @param usuarioDto DTO containing user registration details.
     * @return {@link Mono<ResponseEntity<Usuario>>} containing the created {@link Usuario} on success (HTTP 201 Created),
     *         or an error response on failure (e.g., HTTP 400 Bad Request for invalid input or duplicate email).
     */
    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.CREATED) // Indicates successful creation resource status by default
    public Mono<ResponseEntity<Usuario>> registrarUsuario(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.registrarUsuario(usuarioDto)
                .map(usuario -> ResponseEntity.status(HttpStatus.CREATED).body(usuario))
                .onErrorResume(IllegalArgumentException.class, e ->
                        // For simplicity, returning a generic bad request.
                        // In a real application, you might want to return a more specific error DTO:
                        // Mono.just(ResponseEntity.badRequest().body(new ErrorDto("REGISTRATION_ERROR", e.getMessage()))));
                        Mono.just(ResponseEntity.badRequest().<Usuario>build())
                );
    }

    /**
     * Endpoint for retrieving a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return {@link Mono<ResponseEntity<Usuario>>} containing the found {@link Usuario} (HTTP 200 OK),
     *         or HTTP 404 Not Found if no user with the given ID exists.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Usuario>> buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id)
                .map(ResponseEntity::ok) // Equivalent to .map(usuario -> ResponseEntity.ok(usuario))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
