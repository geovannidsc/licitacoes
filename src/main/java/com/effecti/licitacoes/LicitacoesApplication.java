package com.effecti.licitacoes;

import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LicitacoesApplication implements CommandLineRunner {

	@Autowired
	private LicitacaoService licitacaoService;

	public static void main(String[] args) {
		SpringApplication.run(LicitacoesApplication.class, args);
	}

	@Override
	public void run(String... args) {


		final String urlBase = "http://comprasnet.gov.br/ConsultaLicitacoes/ConsLicitacaoDia.asp?pagina=";
		String paginahtml = "file:///C:\\Users\\Geovanni\\Desktop\\Codigos\\licitacoes\\src\\main\\resources\\comprasnet.html";

		try {
			licitacaoService.buscarNovasLicitacoes(urlBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
