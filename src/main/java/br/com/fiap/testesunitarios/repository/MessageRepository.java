package br.com.fiap.testesunitarios.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.testesunitarios.model.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    
}
