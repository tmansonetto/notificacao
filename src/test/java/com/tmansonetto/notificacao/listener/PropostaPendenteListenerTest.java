package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.domain.Proposta;
import com.tmansonetto.notificacao.domain.Usuario;
import com.tmansonetto.notificacao.service.Notificador;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PropostaPendenteListenerTest {

    @Test
    void deveNotificarOUsuarioQuandoAPropostaEstiverPendente() {
        Notificador notificador = mock(Notificador.class);
        PropostaPendenteListener listener = new PropostaPendenteListener(notificador);

        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setTelefone("+55 (11) 99999-9999");
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);

        listener.propostaPendente(proposta);

        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);
        verify(notificador).notificar(eq("+55 (11) 99999-9999"), mensagem.capture());
        assertTrue(mensagem.getValue().contains("Maria"));
    }
}
