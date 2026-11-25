package com.tmansonetto.notificacao.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

/**
 * Implementação do {@link Notificador} que envia SMS de verdade pelo <b>Amazon SNS</b>.
 *
 * <p>O mesmo código serve para dois ambientes; só muda o {@link SnsClient} recebido, que é criado
 * em {@code AmazonSnsConfiguration}:
 * <ul>
 *   <li>perfil {@code localstack}: o SMS vai para o SNS simulado no Docker (nenhum celular recebe);</li>
 *   <li>perfil {@code aws}: o SMS vai para a AWS e chega no celular do cliente.</li>
 * </ul>
 */
@Slf4j
@Service
// Gera o construtor que recebe o SnsClient (injeção de dependência pelo construtor).
@RequiredArgsConstructor
// Mesmo perfil do AmazonSnsConfiguration: esta classe só existe quando o SnsClient também existe.
@Profile("localstack | aws")
public class NotificacaoSnsService implements Notificador {

    /** Cliente do SDK da AWS, criado e injetado pelo Spring. */
    private final SnsClient snsClient;

    /**
     * Publica o SMS no SNS.
     *
     * <p>No SNS, enviar um SMS é "publicar" uma mensagem direto para um número de telefone.
     * Montamos o pedido ({@link PublishRequest}), enviamos e recebemos a resposta
     * ({@link PublishResponse}) com o {@code messageId}, um código que identifica o envio na AWS
     * e ajuda a rastrear problemas.
     *
     * @param telefone número do cliente, em qualquer formato (é normalizado antes do envio)
     * @param mensagem texto do SMS
     */
    @Override
    public void notificar(String telefone, String mensagem) {
        PublishRequest request = PublishRequest.builder()
                .phoneNumber(formatarTelefone(telefone))
                .message(mensagem)
                .build();
        // Chamada à AWS. Se falhar (rede, credencial inválida...), lança uma exceção e a mensagem
        // continua no RabbitMQ para nova tentativa.
        PublishResponse response = snsClient.publish(request);
        log.info("SMS publicado no SNS. messageId={}", response.messageId());
    }

    /**
     * Converte o telefone para o formato <b>E.164</b>, o único aceito pelo SNS:
     * {@code +} seguido só de dígitos (código do país + DDD + número).
     *
     * <p>Exemplo: {@code "+55 (11) 99999-9999"} vira {@code "+5511999999999"}.
     * A expressão regular {@code \D} significa "qualquer caractere que não seja dígito", então
     * {@code replaceAll("\\D", "")} apaga espaços, parênteses, traços e o "+" original.
     *
     * <p>Atenção: o número precisa vir cadastrado com o código do país (55 para o Brasil).
     *
     * @param telefone telefone como veio do cadastro
     * @return telefone no formato E.164
     */
    private String formatarTelefone(String telefone) {
        return "+" + telefone.replaceAll("\\D", "");
    }
}
