package br.com.fiap.testesunitarios.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.apache.el.stream.Optional;
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
                .user("Marcio")
                .content("message content")
                .build();
    }

    // Unit Tests
    @Test
    void shouldAllowMessageCreation() {
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
    void shouldAllowMessageSearch() {
        UUID id = UUID.randomUUID();
        Message message = getMessage();
        message.setId(id);
        when(messageRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(message));

        // Act
        
    }

    @Test
    void shouldAllowMessageAlteration() {
        fail("Test not implemented.");
    }

    @Test
    void shouldAllowMessageRemoval() {
        fail("Test not implemented.");
    }
}
