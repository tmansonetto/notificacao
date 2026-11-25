package com.tmansonetto.notificacao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

/**
 * Envia SMS pelo Amazon SNS (ou pelo SNS simulado do LocalStack, conforme o perfil ativo).
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Profile("localstack | aws")
public class NotificacaoSnsService implements Notificador {

    private final SnsClient snsClient;

    @Override
    public void notificar(String telefone, String mensagem) {
        PublishRequest request = PublishRequest.builder()
                .phoneNumber(formatarTelefone(telefone))
                .message(mensagem)
                .build();
        PublishResponse response = snsClient.publish(request);
        log.info("SMS publicado no SNS. messageId={}", response.messageId());
    }

    /**
     * O SNS exige o telefone no formato E.164 (ex.: +5511999999999): só dígitos, com "+" na frente.
     */
    private String formatarTelefone(String telefone) {
        return "+" + telefone.replaceAll("\\D", "");
    }
}
