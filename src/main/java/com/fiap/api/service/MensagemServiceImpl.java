package com.fiap.api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fiap.api.exception.MensagemNotFoundException;
import com.fiap.api.model.Mensagem;
import com.fiap.api.repository.MensagemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService {

    private final MensagemRepository mensagemRepository;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return mensagemRepository.findById(id).orElseThrow( () -> 
                new MensagemNotFoundException("Mensagem nao encontrada."));
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem mensagemAtualizada) {
        Mensagem mensagem = buscarMensagem(id);
        if (!mensagem.getId().equals(mensagemAtualizada.getId()))
            throw new MensagemNotFoundException("Mensagem atualizada nao apresenta o ID correto.");
        mensagem.setConteudo(mensagemAtualizada.getConteudo());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public boolean removerMensagem(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerMensagem'");
    }
}