package br.com.fiap.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.api.model.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {
    
}
