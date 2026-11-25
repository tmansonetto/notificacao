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

/**
 * Teste <b>unitário</b> do {@link PropostaPendenteListener}.
 *
 * <p>"Unitário" porque testa só esta classe, isolada: não sobe o Spring, não conecta no RabbitMQ e
 * não envia SMS. Por isso roda em milissegundos. O {@link Notificador} é substituído por um
 * <b>mock</b> (um objeto falso criado pelo Mockito) que só registra as chamadas que recebeu,
 * para depois conferirmos se o listener fez o que devia.
 */
class PropostaPendenteListenerTest {

    /**
     * Cenário: chega uma proposta pendente da cliente Maria.
     * Resultado esperado: o notificador é chamado com o telefone dela e com uma mensagem que contém o nome dela.
     *
     * <p>O teste segue o padrão <b>Arrange / Act / Assert</b> (preparar, executar, verificar).
     */
    @Test
    void deveNotificarOUsuarioQuandoAPropostaEstiverPendente() {
        // Arrange (preparar): cria o mock e entrega ao listener pelo construtor, como o Spring faria.
        Notificador notificador = mock(Notificador.class);
        PropostaPendenteListener listener = new PropostaPendenteListener(notificador);

        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setTelefone("+55 (11) 99999-9999");
        Proposta proposta = new Proposta();
        proposta.setUsuario(usuario);

        // Act (executar): simula a chegada da mensagem chamando o método diretamente.
        listener.propostaPendente(proposta);

        // Assert (verificar): o ArgumentCaptor "captura" o texto que o listener passou ao notificador.
        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);
        // verify confere que notificar(...) foi chamado exatamente uma vez com esse telefone.
        verify(notificador).notificar(eq("+55 (11) 99999-9999"), mensagem.capture());
        assertTrue(mensagem.getValue().contains("Maria"));
    }
}
