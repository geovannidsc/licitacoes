package com.effecti.licitacoes;

import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class LicitacoesApplication implements CommandLineRunner {

	@Autowired
	private LicitacaoService licitacaoService;

	public static void main(String[] args) {
		SpringApplication.run(LicitacoesApplication.class, args);
	}

	@Override
	public void run(String... args) {


		final String comprasNetUrl = "http://comprasnet.gov.br/ConsultaLicitacoes/ConsLicitacaoDia.asp";
		String paginahtml = "file:///C:\\Users\\Geovanni\\Desktop\\Codigos\\licitacoes\\src\\main\\resources\\comprasnet.html";

		try {
			licitacaoService.buscarNovasLicitacoes(paginahtml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
