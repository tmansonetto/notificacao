package com.tmansonetto.notificacao.listener;

import com.tmansonetto.notificacao.constant.MensagemConstante;
import com.tmansonetto.notificacao.domain.Proposta;
import com.tmansonetto.notificacao.service.Notificador;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * "Ouvinte" da fila de propostas pendentes: é aqui que o trabalho deste microsserviço começa.
 *
 * <p>Sempre que o {@code proposta-app} cadastra uma proposta nova, ele publica a proposta na fila
 * {@code proposta-pendente.ms-notificacao} do RabbitMQ. Esta classe recebe cada mensagem dessa fila
 * e manda um SMS avisando o cliente de que a proposta está em análise.
 *
 * <p>Repare que esta classe não sabe <i>como</i> o SMS é enviado (pelo log, pelo LocalStack ou pela
 * AWS). Ela só pede para um {@link Notificador} enviar. Isso deixa o código desacoplado e fácil de
 * testar (veja {@code PropostaPendenteListenerTest}).
 */
// @Component: o Spring cria uma instância desta classe ao subir a aplicação. Sem isso o
// @RabbitListener abaixo não seria registrado.
@Component
// Lombok: gera um construtor com todos os campos "final". O Spring usa esse construtor para
// entregar (injetar) o Notificador. É a forma recomendada de injeção de dependência.
@RequiredArgsConstructor
public class PropostaPendenteListener {

    /**
     * Quem efetivamente envia a notificação. O Spring escolhe a implementação de acordo com o
     * perfil ativo: {@code NotificadorLog} (padrão) ou {@code NotificacaoSnsService} (localstack/aws).
     */
    private final Notificador notificador;

    /**
     * Chamado automaticamente pelo Spring a cada mensagem que chega na fila.
     *
     * <p>Passo a passo:
     * <ol>
     *   <li>O RabbitMQ entrega a mensagem (um JSON);</li>
     *   <li>O conversor configurado em {@code RabbitMQConfiguration} transforma o JSON em {@link Proposta};</li>
     *   <li>Este método monta o texto do SMS com o nome do cliente;</li>
     *   <li>O {@link Notificador} envia o SMS para o telefone do cliente.</li>
     * </ol>
     *
     * <p>Se este método terminar sem erro, o Spring confirma (<i>ack</i>) a mensagem e o RabbitMQ a
     * remove da fila. Se lançar uma exceção, a mensagem não é perdida: o RabbitMQ pode reentregá-la.
     *
     * @param proposta a proposta recebida da fila, já convertida de JSON para objeto Java
     */
    // O nome da fila não fica fixo no código: "${...}" busca o valor da chave
    // rabbit.queue.proposta.pendente no application.properties.
    @RabbitListener(queues = "${rabbit.queue.proposta.pendente}")
    public void propostaPendente(Proposta proposta) {
        // Preenche o %s do modelo com o nome do cliente.
        String mensagem = String.format(MensagemConstante.PROPOSTA_EM_ANALISE, proposta.getUsuario().getNome());
        notificador.notificar(proposta.getUsuario().getTelefone(), mensagem);
    }
}
