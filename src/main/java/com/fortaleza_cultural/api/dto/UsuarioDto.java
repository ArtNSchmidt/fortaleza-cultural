package com.fortaleza_cultural.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private String nome;
    private String email;
    private String senha;
    private String tipo;
}
