package com.fiap.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        UUID id = UUID.randomUUID();
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
        UUID id = UUID.randomUUID();
        when(mensagemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> mensagemService.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem nao encontrada.");
        verify(mensagemRepository, times(1)).findById(id);
    }

    @Test
    void deveAlterarMensagem() {
        UUID id = UUID.randomUUID();
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
        UUID id = UUID.randomUUID();
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
        UUID id = UUID.randomUUID();
        Mensagem mensagemAntiga = getMessage();
        mensagemAntiga.setId(id);
        
        Mensagem mensagemNova = new Mensagem();
        mensagemNova.setId(UUID.randomUUID());
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
        fail("teste nao implementado");
    }

    @Test
    void deveLancarExcecaoQuandoAlterarMensagem() {
        fail("teste nao implementado");
    }


    private Mensagem getMessage() {
        return Mensagem.builder()
                .usuario("Marcio")
                .conteudo("message content")
                .build();
    }
}
