package com.fiap.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.api.model.Mensagem;

@ExtendWith(MockitoExtension.class)
public class MensagemRepositoryTest {
    
    // Mocks setup
    @Mock
    private MensagemRepository messageRepository;

    // Other Methods
    private Mensagem getMessage() {
        return Mensagem.builder()
                .id(UUID.randomUUID())
                .usuario("Marcio")
                .conteudo("message content")
                .build();
    }

    // Unit Tests
    @Test
    void shouldSaveMessage() {
        // Arrange
        Mensagem message = getMessage();
        when(messageRepository.save(message)).thenReturn(message);
        
        // Act
        Mensagem storedMessage = messageRepository.save(message);
        
        // Assert
        assertThat(storedMessage).isNotNull().isEqualTo(message);
        verify(messageRepository, times(1)).save(message);
    }

    @Test 
    void shouldFindMessageById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Mensagem message = getMessage();
        message.setId(id);
        when(messageRepository.findById(id)).thenReturn(Optional.of(message));

        // Act
        Optional<Mensagem> optionalReceivedMessage = messageRepository.findById(id);
        
        // Assert
        assertThat(optionalReceivedMessage).isPresent();
        optionalReceivedMessage.ifPresent(receivedMessage -> {
            assertThat(receivedMessage.getId()).isEqualTo(message.getId());
            assertThat(receivedMessage.getConteudo()).isEqualTo(message.getConteudo());
        });

        verify(messageRepository, times(1)).findById(id);
    }

    @Test
    void shouldDeleteMessageById() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(messageRepository).deleteById(id);

        // Act
        messageRepository.deleteById(id);
        // Assert
        verify(messageRepository, times(1))
                .deleteById(id);
    }

    @Test
    void shouldListMessages() {
        // Arrange
        Mensagem message1 = getMessage();
        Mensagem message2 = getMessage();
        List<Mensagem> messages = Arrays.asList(message1, message2);
        when(messageRepository.findAll()).thenReturn(messages);
        
        // Act
        List<Mensagem> receivedMessages = messageRepository.findAll();
        
        // Assert
        assertThat(receivedMessages)
            .hasSize(2)
            .containsExactlyInAnyOrder(message1, message2);
        verify(messageRepository, times(1)).findAll();
    }
}
