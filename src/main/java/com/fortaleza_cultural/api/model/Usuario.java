package com.fortaleza_cultural.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("usuario")
public class Usuario {

    @Id
    private Long id;

    @Column("nome")
    private String nome;

    @Column("email")
    private String email;

    @Column("senha_hash")
    private String senhaHash;

    @Column("tipo")
    private TipoUsuario tipo;

    @Column("criado_em")
    private LocalDateTime criadoEm;
}
