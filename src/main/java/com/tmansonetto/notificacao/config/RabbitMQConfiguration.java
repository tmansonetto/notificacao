package com.tmansonetto.notificacao.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configurações do RabbitMQ.
 *
 * <h2>O que é o RabbitMQ?</h2>
 * É um <i>message broker</i>: um "correio" entre sistemas. Um sistema (o produtor) deposita
 * mensagens em uma <b>fila</b>, e outro sistema (o consumidor) retira e processa essas mensagens
 * no seu próprio ritmo. Aqui o produtor é o {@code proposta-app} e o consumidor é este microsserviço.
 *
 * <p>Os dados de conexão (host, porta, usuário e senha) ficam no {@code application.properties},
 * nas chaves {@code spring.rabbitmq.*}. O Spring Boot lê essas chaves e cria a conexão sozinho,
 * então aqui só precisamos personalizar o que for diferente do padrão.
 */
// @Configuration indica ao Spring que esta classe declara beans com métodos @Bean.
@Configuration
public class RabbitMQConfiguration {

    /**
     * Define como as mensagens da fila são convertidas em objetos Java.
     *
     * <p>Por padrão, o Spring AMQP só entende texto simples ou objetos Java serializados.
     * Como o {@code proposta-app} envia a proposta em formato <b>JSON</b>, registramos um conversor
     * que usa a biblioteca <b>Jackson</b> (versão 3) para transformar o JSON em objeto Java.
     *
     * <p>Exemplo: a mensagem {@code {"id":1,"usuario":{"nome":"Maria"}}} vira um objeto
     * {@code Proposta} com {@code getUsuario().getNome()} igual a "Maria", e chega pronta como
     * parâmetro no método do {@code PropostaPendenteListener}.
     *
     * @return o conversor JSON que o Spring vai usar em todos os {@code @RabbitListener}
     */
    // @Bean: o objeto retornado passa a ser gerenciado pelo Spring. Ao encontrar um bean do tipo
    // MessageConverter, o Spring Boot passa a usá-lo automaticamente no lugar do conversor padrão.
    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
