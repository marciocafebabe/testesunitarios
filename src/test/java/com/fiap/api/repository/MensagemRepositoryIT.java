package com.fiap.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.fiap.api.model.Mensagem;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MensagemRepositoryIT {
    
    @Autowired
    private MensagemRepository mensagemRepository;

    private Mensagem getMensagem() {
        return Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("Marcio")
                .conteudo("message content")
                .gostei(0)
                .build();
    }

    private Mensagem saveMessage(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }

    @Test
    void shouldCreateTable() {
        Long registersCount = mensagemRepository.count();
        assertThat(registersCount).isGreaterThan(0);
    }

    @Test 
    void shouldSaveMessage() {
        // Arrange
        UUID id = UUID.fromString("166f0302-2cd6-472b-8e60-616ff3f9b2cf");
        Mensagem mensagem = getMensagem();
        mensagem.setId(id);
        
        // Act
        Mensagem receivedMessage = saveMessage(mensagem);

        // Assert
        assertThat(receivedMessage).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(receivedMessage.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(receivedMessage.getUsuario()).isEqualTo(mensagem.getUsuario());
    }

    @Test
    void shouldGetMessageById() {
        // Arrange
        UUID id = UUID.fromString("166f0302-2cd6-472b-8e60-616ff3f9b2cf");

        // Act
        Optional<Mensagem> repositoryResponse = mensagemRepository.findById(id);

        // Assert
        assertThat(repositoryResponse).isPresent();
        repositoryResponse.ifPresent(receivedMessage -> {
            assertThat(receivedMessage.getId()).isEqualTo(id);
        });
    }

    @Test
    void shouldDeleteMessageById() {
        // Arrange
        UUID id = UUID.fromString("166f0302-2cd6-472b-8e60-616ff3f9b2cf");

        // Initial assertion
        Optional<Mensagem> repositoryResponse = mensagemRepository.findById(id);
        assertThat(repositoryResponse).isPresent();
        
        // Act
        mensagemRepository.deleteById(id);
        repositoryResponse = mensagemRepository.findById(id);

        // Actual feature assertion
        assertThat(repositoryResponse).isNotPresent();
    }

    @Test
    void shouldListMessages() {
        List<Mensagem> repositoryResponse = mensagemRepository.findAll();
        assertThat(repositoryResponse).isNotEmpty();
        assertThat(repositoryResponse.size()).isGreaterThan(1);
    }

}
