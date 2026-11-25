package com.tmansonetto.notificacao.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa o cliente que fez a proposta, do jeito que ele chega dentro do JSON da {@link Proposta}.
 *
 * <p>Para enviar o SMS, este microsserviço só precisa do {@link #nome} (para personalizar o texto)
 * e do {@link #telefone} (o destino). Os demais campos vêm junto no JSON e ficam disponíveis
 * caso uma notificação futura precise deles.
 */
// Lombok gera os getters e setters (veja o comentário em Proposta).
@Getter
@Setter
public class Usuario {

    /** Identificador do usuário no sistema de origem. */
    private Long id;

    /** Primeiro nome; usado no texto do SMS ("Prezado(a) Maria, ..."). */
    private String nome;

    private String sobrenome;

    private String cpf;

    /** Celular do cliente; é para este número que o SMS é enviado. */
    private String telefone;

    /** Renda informada pelo cliente, usada na análise de crédito. */
    private Double renda;

    /**
     * Referência de volta para a proposta. Atenção: Proposta aponta para Usuario e Usuario aponta
     * para Proposta (referência circular). Na hora de <i>gerar</i> o JSON, o lado que envia
     * (proposta-app) precisa ignorar um dos lados (ex.: com {@code @JsonIgnore}); senão o JSON
     * ficaria infinito.
     */
    private Proposta proposta;
}
