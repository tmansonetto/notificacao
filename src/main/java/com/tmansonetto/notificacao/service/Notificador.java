package com.tmansonetto.notificacao.service;

/**
 * Contrato (interface) para enviar uma notificação por SMS a um usuário.
 *
 * <h2>Por que uma interface?</h2>
 * Uma interface diz <i>o que</i> deve ser feito, sem dizer <i>como</i>. Quem usa o
 * {@code Notificador} (o {@code PropostaPendenteListener}) depende só deste contrato, e não de uma
 * implementação específica. Assim podemos trocar a forma de envio sem mexer em quem chama. É o
 * princípio da inversão de dependência (o "D" do SOLID).
 *
 * <p>Há duas implementações, e o Spring escolhe uma delas de acordo com o <b>perfil</b> ativo:
 * <ul>
 *   <li>perfil padrão (nenhum): {@link NotificadorLog}, apenas escreve no log (sem custo);</li>
 *   <li>perfil {@code localstack}: {@link NotificacaoSnsService} usando o SNS simulado do LocalStack;</li>
 *   <li>perfil {@code aws}: {@link NotificacaoSnsService} usando o Amazon SNS de verdade.</li>
 * </ul>
 * Os perfis nunca se sobrepõem, então sempre existe exatamente uma implementação ativa.
 */
public interface Notificador {

    /**
     * Envia uma mensagem de texto para um telefone.
     *
     * @param telefone número de destino, como veio do cadastro (pode ter espaços, parênteses e traços)
     * @param mensagem texto já pronto para enviar
     */
    void notificar(String telefone, String mensagem);
}
