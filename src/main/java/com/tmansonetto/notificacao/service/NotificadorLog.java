package com.tmansonetto.notificacao.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementação de desenvolvimento do {@link Notificador}: não envia nada, só escreve a mensagem no log.
 *
 * <p>Serve para rodar e testar o fluxo inteiro (RabbitMQ → listener → notificação) na sua máquina
 * sem precisar de conta na AWS, sem Docker do LocalStack e sem gastar com SMS. Basta olhar o console
 * para ver o "SMS" que seria enviado.
 */
// Lombok: cria um campo "log" pronto para escrever mensagens no log (log.info, log.error...).
@Slf4j
// @Service: igual ao @Component (o Spring cria e gerencia a instância), mas indica que a classe
// pertence à camada de serviço, onde ficam as regras e ações do sistema.
@Service
// Ativa quando NÃO estiver no perfil "localstack" E NÃO estiver no perfil "aws".
// Ou seja: é o comportamento padrão quando você roda a aplicação sem escolher perfil nenhum.
@Profile("!localstack & !aws")
public class NotificadorLog implements Notificador {

    /**
     * Simula o envio escrevendo telefone e mensagem no log.
     * Os {@code {}} são substituídos, em ordem, pelos argumentos seguintes.
     */
    @Override
    public void notificar(String telefone, String mensagem) {
        log.info("[SMS simulado] para {}: {}", telefone, mensagem);
    }
}
