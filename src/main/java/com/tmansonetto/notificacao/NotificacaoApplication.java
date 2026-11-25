package com.tmansonetto.notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do microsserviço de notificação.
 *
 * <h2>O que este microsserviço faz?</h2>
 * Ele faz parte do sistema de propostas de crédito. O sistema principal ({@code proposta-app})
 * recebe as propostas dos clientes e, quando algo importante acontece (por exemplo, uma proposta
 * nova ficou pendente de análise), publica uma mensagem no <b>RabbitMQ</b>.
 * Este microsserviço fica "escutando" essas mensagens e avisa o cliente por <b>SMS</b>.
 *
 * <h2>Fluxo completo</h2>
 * <pre>
 *  proposta-app ──(JSON)──▶ RabbitMQ (fila) ──▶ PropostaPendenteListener ──▶ Notificador ──▶ SMS
 * </pre>
 *
 * <h2>Por que separar em um microsserviço?</h2>
 * Enviar SMS não é responsabilidade do sistema de propostas. Separando, cada parte pode ser
 * desenvolvida, publicada e escalada de forma independente. Se o serviço de SMS cair, as
 * mensagens ficam guardadas na fila e são processadas quando ele voltar, sem perder nada.
 */
// @SpringBootApplication junta três anotações em uma só:
//  - @Configuration: esta classe pode declarar beans (objetos gerenciados pelo Spring);
//  - @EnableAutoConfiguration: o Spring Boot configura sozinho o que encontrar no classpath
//    (ex.: achou a biblioteca do RabbitMQ, então cria a conexão com o RabbitMQ);
//  - @ComponentScan: procura classes anotadas (@Component, @Service, @Configuration...) neste
//    pacote e nos subpacotes. Por isso todas as classes ficam dentro de com.tmansonetto.notificacao.
@SpringBootApplication
public class NotificacaoApplication {

	/**
	 * Método que a JVM executa ao iniciar a aplicação.
	 * O {@link SpringApplication#run} sobe o Spring: cria todos os beans, conecta no RabbitMQ
	 * e começa a consumir as filas. A aplicação fica rodando até ser encerrada.
	 *
	 * @param args argumentos de linha de comando; por exemplo,
	 *             {@code --spring.profiles.active=localstack} escolhe o perfil de execução.
	 */
	public static void main(String[] args) {
		SpringApplication.run(NotificacaoApplication.class, args);
	}

}
