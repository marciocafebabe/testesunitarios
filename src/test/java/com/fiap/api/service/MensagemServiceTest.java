package com.fiap.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import java.util.Arrays;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fiap.api.exception.MensagemNotFoundException;
import com.fiap.api.model.Mensagem;
import com.fiap.api.repository.MensagemRepository;

class MensagemServiceTest {
    
    private MensagemService mensagemService;

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImpl(mensagemRepository);
    }

    void tearDown() throws Exception {
        mock.close();
    }


    @Test
    void deveRegistrarMensagem() {
        Mensagem mensagem = getMessage();
        when(mensagemRepository.save(any(Mensagem.class)))
                .thenAnswer(i -> i.getArgument(0));

        Mensagem mensagemRegistrada = mensagemService
                .registrarMensagem(mensagem);
        
        assertThat(mensagemRegistrada)
                .isInstanceOf(Mensagem.class)
                .isNotNull();
        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagem.getUsuario()).isEqualTo(mensagem.getUsuario());
        assertThat(mensagem.getId()).isNotNull();
        verify(mensagemRepository, times(1)).save(mensagem);
    }

    @Test
    void deveBuscarMensagem() {
        UUID id = UUID.fromString("bd2e7fee-e6d7-43d1-a046-f2592e03e3ae");
        Mensagem mensagem = getMessage();
        mensagem.setId(id);
        when(mensagemRepository.findById(id))
                .thenReturn(Optional.of(mensagem));

        Mensagem mensagemObtida = mensagemService.buscarMensagem(id);

        assertThat(mensagemObtida).isEqualTo(mensagemObtida);
        verify(mensagemRepository, times(1)).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoBuscarMensagem() {
        UUID id = UUID.fromString("b88ae23e-4e79-4f82-b9ff-376e0c2aefc5");
        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> mensagemService.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem nao encontrada.");
        verify(mensagemRepository, times(1)).findById(id);
    }

    @Test
    void deveAlterarMensagem() {
        UUID id = UUID.fromString("bef59be9-d131-47c7-a7d9-e62ea1fc7971");
        Mensagem mensagemAntiga = getMessage();
        mensagemAntiga.setId(id);
        Mensagem mensagemNova = new Mensagem();
        mensagemNova.setUsuario(mensagemAntiga.getUsuario());
        mensagemNova.setId(id);
        mensagemNova.setConteudo("Conteudo mensagemNova");

        when(mensagemRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(mensagemNova));
        when(mensagemRepository.save(mensagemNova))
                .thenAnswer(i -> i.getArgument(0));

        Mensagem mensagemObtida = mensagemService.alterarMensagem(id, mensagemNova);

        assertThat(mensagemObtida).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemObtida.getId()).isEqualTo(mensagemNova.getId());
        assertThat(mensagemObtida.getConteudo()).isEqualTo(mensagemNova.getConteudo());
        assertThat(mensagemObtida.getUsuario()).isEqualTo(mensagemNova.getUsuario());
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, times(1)).save(any(Mensagem.class));
    }
    
    @Test
    void deveGerarExcecaoAoAlterarMensagemQuandoFalhaNaBusca() {
        UUID id = UUID.fromString("c139a690-6fc2-45c4-86b9-ad39374c8b4e");
        Mensagem mensagem = getMessage();
        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy( () -> mensagemService.alterarMensagem(id, mensagem))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem nao encontrada.");
        verify(mensagemRepository, times(1)).findById(id);
        verify(mensagemRepository, never()).save(mensagem);
    }

    @Test
    void deveGerarExcecaoAoAlterarMensagemQuandoIdDiferente() {
        UUID id = UUID.fromString("1d2a63db-356f-4c06-90e8-1d3c2832c7cd");
        Mensagem mensagemAntiga = getMessage();
        mensagemAntiga.setId(id);
        
        Mensagem mensagemNova = new Mensagem();
        mensagemNova.setId(
                UUID.fromString("2a8c12d5-6577-4fca-b7f2-efad1f77c592"));
        mensagemNova.setConteudo("novo");

        when(mensagemRepository.findById(id)).thenReturn(Optional.of(mensagemAntiga));
        assertThatThrownBy( () -> mensagemService.alterarMensagem(id, mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem atualizada nao apresenta o ID correto.");
        verify(mensagemRepository, times(1)).findById(id);
        verify(mensagemRepository, never()).save(mensagemAntiga);
    }

    @Test
    void deveRemoverMensagem() {
        UUID id = UUID.fromString("4e213700-fead-42f1-b7ce-0cabd6e0be69");
        Mensagem mensagem = getMessage();
        mensagem.setId(id);
        when(mensagemRepository.findById(id))
                .thenReturn(Optional.of(mensagem));
        doNothing().when(mensagemRepository).deleteById(id);
        Boolean mensagemFoiRemovida = mensagemService.removerMensagem(id);
        assertThat(mensagemFoiRemovida).isTrue();
        verify(mensagemRepository, times(1)).findById(any(UUID.class));
        verify(mensagemRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deveGerarExcecaoAoRemoverMensagemQuandoIdNaoExiste() {
        UUID id = UUID.fromString("ffcdeaf7-8671-4903-b245-47a0bd1d2ad1");
        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());
        
        assertThatThrownBy( () -> mensagemService.removerMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem nao encontrada.");
                verify(mensagemRepository, times(1)).findById(id);
                verify(mensagemRepository, never()).deleteById(id);
    }    

    @Test
    void deveListarMensagens() {
        Mensagem message1 = getMessage();
        Mensagem message2 = getMessage();
        Page<Mensagem> mensagens = new PageImpl<>(Arrays.asList(
                message1, 
                message2
        ));
        when(mensagemRepository.listarMensagens(any(Pageable.class)))
                .thenReturn(mensagens);
        
        var resultadoObtido = mensagemService.listarMensagens(Pageable.unpaged());

        assertThat(resultadoObtido).hasSize(2);
        assertThat(resultadoObtido.getContent())
                .asInstanceOf(InstanceOfAssertFactories.LIST) 
                .allSatisfy(mensagem -> {
                        assertThat(mensagem)
                                .isNotNull()
                                .isInstanceOf(Mensagem.class);
                });
        verify(mensagemRepository, times(1))
                .listarMensagens(any(Pageable.class));
    }


    private Mensagem getMessage() {
        return Mensagem.builder()
                .usuario("Marcio")
                .conteudo("message content")
                .build();
    }
}
