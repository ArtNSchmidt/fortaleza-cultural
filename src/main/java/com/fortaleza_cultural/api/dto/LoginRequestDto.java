package com.fortaleza_cultural.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user login requests.
 * Contains the credentials required for authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's password.
     */
    private String senha;
}
