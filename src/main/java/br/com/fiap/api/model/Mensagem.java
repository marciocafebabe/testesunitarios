package br.com.fiap.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @Id
    private UUID id;

    @Column(nullable = false)
    @NotEmpty(message = "Username can't be empty.")
    private String usuario;

    @Column(nullable = false)
    @NotEmpty(message = "Content can't be empty.")
    private String conteudo;

    @Column(nullable = false)
    @Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    @Default
    private int gostei = 0;

}
