package com.fiap.api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fiap.api.model.Mensagem;

public interface MensagemService {
    
    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem buscarMensagem(UUID id);

    Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada);

    boolean removerMensagem(UUID id);

    Page<Mensagem> listarMensagens(Pageable pageable);
}
