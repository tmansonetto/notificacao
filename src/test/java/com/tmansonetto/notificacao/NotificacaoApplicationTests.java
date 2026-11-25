package com.tmansonetto.notificacao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Teste de <b>integração</b> básico: verifica se a aplicação inteira consegue subir.
 *
 * <p>{@code @SpringBootTest} inicia o Spring de verdade, com todos os beans e configurações.
 * Se faltar uma configuração, uma propriedade ou houver um bean com problema, o teste falha.
 * É uma rede de segurança simples contra erros de configuração.
 */
@SpringBootTest
class NotificacaoApplicationTests {

	/**
	 * Fica vazio de propósito: o teste passa se o contexto do Spring carregar sem erro.
	 * ("context loads" = "o contexto carrega".)
	 */
	@Test
	void contextLoads() {
	}

}
