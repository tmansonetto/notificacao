package com.tmansonetto.notificacao.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Representa uma proposta de crédito, do jeito que ela chega pela fila do RabbitMQ.
 *
 * <p>Esta classe é uma "cópia" simplificada da entidade {@code Proposta} do {@code proposta-app}.
 * Cada microsserviço tem suas próprias classes: aqui não há banco de dados nem JPA, a classe serve
 * só para receber os dados do JSON. Para a conversão funcionar, os <b>nomes dos campos</b> precisam
 * ser iguais aos nomes no JSON enviado pelo {@code proposta-app}. Campos do JSON que não existirem
 * aqui são simplesmente ignorados.
 */
// Lombok: gera automaticamente, na compilação, os métodos getX() e setX() de todos os campos.
// Sem ele, teríamos que escrever dezenas de linhas repetitivas à mão.
// O Jackson usa esses setters para preencher o objeto a partir do JSON.
@Setter
@Getter
public class Proposta {

    /** Identificador da proposta no sistema de origem. */
    private Long id;

    /** Valor do empréstimo pedido pelo cliente. */
    private Double valorSolicitado;

    /** Prazo para pagamento (quantidade de parcelas/meses). */
    private int prazoPagamento;

    /**
     * Resultado da análise: {@code true} = aprovada, {@code false} = negada,
     * {@code null} = ainda não analisada. Por isso é {@code Boolean} (objeto) e não {@code boolean}
     * (primitivo), que não aceita {@code null}.
     */
    private Boolean aprovado;

    /** Indica se a proposta já foi enviada (integrada) para a fila. */
    private boolean integrada;

    /** Observação da análise, por exemplo o motivo de uma proposta negada. */
    private String observacao;

    /** Cliente dono da proposta. É dele que tiramos o nome e o telefone para o SMS. */
    private Usuario usuario;
}
