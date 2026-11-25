package com.tmansonetto.notificacao.constant;

/**
 * Textos das mensagens SMS enviadas aos clientes.
 *
 * <p>Concentrar os textos em um só lugar facilita a manutenção: para mudar a redação de um SMS,
 * basta alterar aqui, sem procurar pelo código.
 *
 * <p>Os textos são <b>modelos</b>: cada {@code %s} é um espaço que será preenchido depois com
 * {@link String#format(String, Object...)}. Exemplo:
 * <pre>
 * String.format(PROPOSTA_APROVADA, "Maria")
 * // "Prezado(a) Maria, sua proposta foi APROVADA. Em breve receberá mais informações."
 * </pre>
 *
 * <p>{@code public static final} cria uma <b>constante</b>: pública (qualquer classe pode usar),
 * estática (pertence à classe, não precisa de {@code new}) e final (o valor não pode mudar).
 */
public class MensagemConstante {

    /** Enviada quando a proposta chega e fica pendente de análise. {@code %s} = nome do cliente. */
    public static final String PROPOSTA_EM_ANALISE = "Prezado(a) %s, sua proposta foi recebida por nossa equipe e será analisada. Em breve retornaremos o contato.";

    /** Enviada quando a proposta é negada. 1º {@code %s} = nome do cliente; 2º {@code %s} = motivo. */
    public static final String PROPOSTA_NEGADA = "Prezado(a) %s, sua proposta foi NEGADA. Nosso sistema detectou a seguinte causa: %s";

    /** Enviada quando a proposta é aprovada. {@code %s} = nome do cliente. */
    public static final String PROPOSTA_APROVADA = "Prezado(a) %s, sua proposta foi APROVADA. Em breve receberá mais informações.";
}
