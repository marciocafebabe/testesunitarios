package com.fiap.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.api.model.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {
    
}
