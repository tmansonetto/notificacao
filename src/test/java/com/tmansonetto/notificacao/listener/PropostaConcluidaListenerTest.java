package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.constant.MensagemConstante;
import com.tmansonetto.notificacao.domain.Proposta;
import com.tmansonetto.notificacao.domain.Usuario;
import com.tmansonetto.notificacao.service.Notificador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Teste <b>unitário</b> do {@link PropostaConcluidaListener} (veja a explicação em
 * {@code PropostaPendenteListenerTest}).
 */
class PropostaConcluidaListenerTest {

    private static final String TELEFONE = "+55 (11) 99999-9999";

    private Notificador notificador;
    private PropostaConcluidaListener listener;
    private Proposta proposta;

    /** Executado antes de cada teste: prepara um listener e uma proposta da cliente Maria. */
    @BeforeEach
    void setUp() {
        notificador = mock(Notificador.class);
        listener = new PropostaConcluidaListener(notificador);

        Usuario usuario = new Usuario();
        usuario.setNome("Maria");
        usuario.setTelefone(TELEFONE);
        proposta = new Proposta();
        proposta.setUsuario(usuario);
    }

    @Test
    void deveEnviarMensagemDeAprovacaoQuandoAPropostaForAprovada() {
        proposta.setAprovado(true);

        listener.propostaConcluida(proposta);

        verify(notificador).notificar(TELEFONE, String.format(MensagemConstante.PROPOSTA_APROVADA, "Maria"));
    }

    @Test
    void deveEnviarMensagemDeNegacaoComOMotivoQuandoAPropostaForNegada() {
        proposta.setAprovado(false);
        proposta.setObservacao("renda insuficiente");

        listener.propostaConcluida(proposta);

        verify(notificador).notificar(TELEFONE,
                String.format(MensagemConstante.PROPOSTA_NEGADA, "Maria", "renda insuficiente"));
    }
}
