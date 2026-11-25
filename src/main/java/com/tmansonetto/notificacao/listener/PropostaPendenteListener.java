package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.constant.MensagemConstante;
import com.tmansonetto.notificacao.domain.Proposta;
import com.tmansonetto.notificacao.service.Notificador;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PropostaPendenteListener {

    private final Notificador notificador;

    @RabbitListener(queues = "${rabbit.queue.proposta.pendente}")
    public void propostaPendente(Proposta proposta) {
        String mensagem = String.format(MensagemConstante.PROPOSTA_EM_ANALISE, proposta.getUsuario().getNome());
        notificador.notificar(proposta.getUsuario().getTelefone(), mensagem);
    }
}
