package com.tmansonetto.notificacao.service;

/**
 * Contrato para enviar uma notificação (SMS) a um usuário.
 *
 * <p>Há três implementações, escolhidas pelo perfil do Spring:
 * <ul>
 *   <li>perfil padrão: {@link NotificadorLog}, apenas escreve no log (sem custo);</li>
 *   <li>perfil {@code localstack}: {@link NotificacaoSnsService} usando o SNS simulado do LocalStack;</li>
 *   <li>perfil {@code aws}: {@link NotificacaoSnsService} usando o Amazon SNS de verdade.</li>
 * </ul>
 */
public interface Notificador {

    void notificar(String telefone, String mensagem);
}
