package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.domain.Proposta;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PropostaPendenteListener {
    @RabbitListener(queues ="${rabbit.queue.proposta.pendente}" )
    public void propostaPendente(Proposta proposta) {}
}
