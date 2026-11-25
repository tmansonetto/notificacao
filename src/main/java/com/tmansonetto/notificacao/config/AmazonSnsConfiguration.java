package com.tmansonetto.notificacao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;

import java.net.URI;

/**
 * Cria o cliente que conversa com o <b>Amazon SNS</b>.
 *
 * <h2>O que é o Amazon SNS?</h2>
 * O SNS (Simple Notification Service) é um serviço da AWS para enviar notificações,
 * entre elas <b>SMS</b> para celulares. Para usá-lo pelo Java, usamos o AWS SDK v2, que oferece
 * a classe {@link SnsClient}.
 *
 * <h2>Perfis (profiles) do Spring</h2>
 * Esta classe só é carregada quando o perfil ativo é {@code localstack} ou {@code aws}:
 * <ul>
 *   <li>{@code localstack}: usa o LocalStack, um "AWS de mentira" que roda no Docker
 *       ({@code docker compose up -d}). Bom para testar sem gastar dinheiro;</li>
 *   <li>{@code aws}: usa a AWS de verdade (envia SMS reais e é cobrado).</li>
 * </ul>
 * Sem nenhum desses perfis, esta classe é ignorada e o SMS só aparece no log (veja {@code NotificadorLog}).
 *
 * <p>Os valores abaixo vêm de {@code application-localstack.properties} ou de
 * {@code application-aws.properties}, conforme o perfil ativo.
 */
@Configuration
// @Profile: só cria esta configuração se o perfil "localstack" OU o perfil "aws" estiver ativo.
// Para ativar: --spring.profiles.active=localstack (ou a variável SPRING_PROFILES_ACTIVE).
@Profile("localstack | aws")
public class AmazonSnsConfiguration {

    // @Value("${chave}") injeta no campo o valor da chave lida do arquivo .properties.
    // Na AWS real, essas chaves apontam para variáveis de ambiente, assim a senha nunca vai para o Git.

    /** Identificação da conta AWS (equivale a um "usuário"). */
    @Value("${aws.access-key}")
    private String accessKey;

    /** Chave secreta da conta AWS (equivale a uma "senha"). Nunca deve ser escrita no código. */
    @Value("${aws.secret-key}")
    private String secretKey;

    /** Região da AWS onde o serviço será usado. O ":us-east-1" é o valor padrão se a chave não existir. */
    @Value("${aws.region:us-east-1}")
    private String region;

    /**
     * Endereço do serviço. Fica vazio na AWS de verdade (o SDK descobre o endereço sozinho pela região);
     * no LocalStack aponta para {@code http://localhost:4566}.
     * O ":" sem nada depois significa "valor padrão = texto vazio".
     */
    @Value("${aws.endpoint:}")
    private String endpoint;

    /**
     * Monta o {@link SnsClient} e o registra como bean do Spring.
     *
     * <p>Depois de criado, qualquer classe que pedir um {@code SnsClient} no construtor
     * (como o {@code NotificacaoSnsService}) recebe este mesmo objeto, sem precisar criá-lo.
     * Isso se chama <b>injeção de dependência</b>.
     *
     * @return o cliente pronto para publicar mensagens no SNS
     */
    @Bean
    public SnsClient snsClient() {
        // O padrão "builder" monta o objeto passo a passo: região, credenciais e, por fim, build().
        SnsClientBuilder builder = SnsClient.builder()
                .region(Region.of(region))
                // Credenciais fixas (estáticas), lidas do .properties.
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)));

        // Só troca o endereço do serviço quando um endpoint foi informado (caso do LocalStack).
        if (!endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }
        return builder.build();
    }
}
