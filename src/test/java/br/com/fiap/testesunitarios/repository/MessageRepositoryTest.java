package br.com.fiap.testesunitarios.repository;

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

import br.com.fiap.testesunitarios.model.Message;

@ExtendWith(MockitoExtension.class)
public class MessageRepositoryTest {
    
    // Mocks setup
    @Mock
    private MessageRepository messageRepository;

    // Other Methods
    private Message getMessage() {
        return Message.builder()
                .id(UUID.randomUUID())
                .username("Marcio")
                .content("message content")
                .build();
    }

    // Unit Tests
    @Test
    void shouldSaveMessage() {
        // Arrange
        Message message = getMessage();
        when(messageRepository.save(message)).thenReturn(message);
        
        // Act
        Message storedMessage = messageRepository.save(message);
        
        // Assert
        assertThat(storedMessage).isNotNull().isEqualTo(message);
        verify(messageRepository, times(1)).save(message);
    }

    @Test 
    void shouldFindMessageById() {
        // Arrange
        UUID id = UUID.randomUUID();
        Message message = getMessage();
        message.setId(id);
        when(messageRepository.findById(id)).thenReturn(Optional.of(message));

        // Act
        Optional<Message> optionalReceivedMessage = messageRepository.findById(id);
        
        // Assert
        assertThat(optionalReceivedMessage).isPresent();
        optionalReceivedMessage.ifPresent(receivedMessage -> {
            assertThat(receivedMessage.getId()).isEqualTo(message.getId());
            assertThat(receivedMessage.getContent()).isEqualTo(message.getContent());
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
        Message message1 = getMessage();
        Message message2 = getMessage();
        List<Message> messages = Arrays.asList(message1, message2);
        when(messageRepository.findAll()).thenReturn(messages);
        
        // Act
        List<Message> receivedMessages = messageRepository.findAll();
        
        // Assert
        assertThat(receivedMessages)
            .hasSize(2)
            .containsExactlyInAnyOrder(message1, message2);
        verify(messageRepository, times(1)).findAll();
    }
}
