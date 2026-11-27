package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.constant.MensagemConstante;
import com.tmansonetto.notificacao.domain.Proposta;
import com.tmansonetto.notificacao.service.Notificador;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * "Ouvinte" da fila de propostas concluídas: avisa o cliente do resultado da análise de crédito.
 *
 * <p>Quando a análise de uma proposta termina, o resultado é publicado na fila
 * {@code proposta-concluida.ms-notificacao} do RabbitMQ. Esta classe recebe cada mensagem dessa fila
 * e manda um SMS dizendo se a proposta foi <b>aprovada</b> ou <b>negada</b> (com o motivo).
 *
 * <p>Assim como o {@link PropostaPendenteListener}, esta classe não sabe <i>como</i> o SMS é
 * enviado: ela só pede para um {@link Notificador} enviar (veja {@code PropostaConcluidaListenerTest}).
 */
@Component
@RequiredArgsConstructor
public class PropostaConcluidaListener {

    /** Quem efetivamente envia a notificação (implementação escolhida pelo perfil ativo). */
    private final Notificador notificador;

    /**
     * Chamado automaticamente pelo Spring a cada mensagem que chega na fila de propostas concluídas.
     *
     * <p>O campo {@code aprovado} da proposta decide qual texto é enviado:
     * <ul>
     *   <li>{@code true}: {@link MensagemConstante#PROPOSTA_APROVADA};</li>
     *   <li>{@code false} (ou {@code null}): {@link MensagemConstante#PROPOSTA_NEGADA}, com a
     *       {@code observacao} da análise como motivo.</li>
     * </ul>
     *
     * @param proposta a proposta já analisada, convertida de JSON para objeto Java
     */
    @RabbitListener(queues = "${rabbit.queue.proposta.concluida}")
    public void propostaConcluida(Proposta proposta) {
        String nome = proposta.getUsuario().getNome();
        // Boolean.TRUE.equals(...) evita NullPointerException caso "aprovado" venha null:
        // nesse caso o resultado é simplesmente false.
        String mensagem = Boolean.TRUE.equals(proposta.getAprovado())
                ? String.format(MensagemConstante.PROPOSTA_APROVADA, nome)
                : String.format(MensagemConstante.PROPOSTA_NEGADA, nome, proposta.getObservacao());
        notificador.notificar(proposta.getUsuario().getTelefone(), mensagem);
    }
}
