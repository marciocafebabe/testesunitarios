package br.com.fiap.testesunitarios.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.fiap.testesunitarios.model.Message;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class MessageRepositoryIT {
    
    @Autowired
    private MessageRepository messageRepository;

    private Message getMessage() {
        return Message.builder()
                .username("Marcio")
                .content("message content")
                .build();
    }

    @Test
    void shouldCreateTable() {
        Long registersCount = messageRepository.count();
        assertThat(registersCount).isNotNegative();
    }

    @Test 
    void shouldSaveMessage() {
        // Arrange
        UUID id = UUID.randomUUID();
        Message message = getMessage();
        message.setId(id);
        
        // Act
        Message receivedMessage = saveMessage(message);

        // Assert
        assertThat(receivedMessage).isInstanceOf(Message.class).isNotNull();
        assertThat(receivedMessage.getContent()).isEqualTo(message.getContent());
        assertThat(receivedMessage.getUsername()).isEqualTo(message.getUsername());
    }

    @Test
    void shouldGetMessageById() {
        UUID id = UUID.randomUUID();
        Message message = getMessage();
        message.setId(id);
        saveMessage(message);

        // Act
        Optional<Message> repositoryResponse = messageRepository.findById(id);

        // Assert
        assertThat(repositoryResponse).isPresent();
        repositoryResponse.ifPresent(receivedMessage -> {
            assertThat(receivedMessage.getId()).isEqualTo(id);
            assertThat(receivedMessage.getContent()).isEqualTo(message.getContent());
        });
    }

    private Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // @Test
    // void shouldDeleteMessageById() {
    //     fail("test not implemented");
    // }

    // @Test
    // void shouldListMessages() {
    //     fail("test not implemented");
    // }

}
