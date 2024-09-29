package com.effecti.licitacoes;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.http.JsoupHttpClient;
import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.scraper.ComprasNetLicitacaoScraper;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class LicitacoesApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(LicitacoesApplication.class, args);

		//File file = new File("C:\\Users\\Geovanni\\Desktop\\pagina");
		//System.out.println("Arquivo existe? " + file.exists());


		final String comprasNetUrl = "http://comprasnet.gov.br/ConsultaLicitacoes/ConsLicitacaoDia.asp";
		String paginahtml = "file:///C:\\Users\\Geovanni\\Desktop\\Codigos\\licitacoes\\src\\main\\resources\\comprasnet.html";



		//System.out.println(paginaatual);
		IHttpClient httpClient = new JsoupHttpClient();

		// Inicializa o scraper específico para o ComprasNet
		ILicitacaoScraper licitacaoScraper = new ComprasNetLicitacaoScraper(httpClient);

		try {
			List<Licitacao> licitacoes = licitacaoScraper.buscarLicitacoes(paginahtml);

			// Exibe as licitações no console
			for (Licitacao licitacao : licitacoes) {
				System.out.println(licitacao);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}



	}

}
