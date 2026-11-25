package com.tmansonetto.notificacao.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementação de desenvolvimento: não envia nada, só registra a mensagem no log.
 * Ativa quando nenhum dos perfis {@code localstack} ou {@code aws} está habilitado.
 */
@Slf4j
@Service
@Profile("!localstack & !aws")
public class NotificadorLog implements Notificador {

    @Override
    public void notificar(String telefone, String mensagem) {
        log.info("[SMS simulado] para {}: {}", telefone, mensagem);
    }
}
