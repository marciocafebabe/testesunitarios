package br.com.fiap.testesunitarios.model;

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
public class Message {
    
    @Id
    private UUID id;

    @Column(nullable = false)
    @NotEmpty(message = "User can't be empty.")
    private String user;

    @Column(nullable = false)
    @NotEmpty(message = "Content can't be empty.")
    private String content;

    @Default
    private LocalDateTime messageCreationTimestamp = LocalDateTime.now();

    @Default
    private int likeCount = 0;

}
