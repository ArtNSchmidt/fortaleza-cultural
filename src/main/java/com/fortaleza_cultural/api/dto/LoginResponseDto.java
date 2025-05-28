package com.fortaleza_cultural.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login responses.
 * Typically contains an authentication token upon successful login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    /**
     * The authentication token (e.g., JWT) issued upon successful login.
     */
    private String token;

    // In the future, you might add more user details here if needed:
    // private UsuarioDto usuarioInfo;
}
