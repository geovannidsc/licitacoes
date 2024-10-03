package com.effecti.licitacoes.factory;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.scraper.ComprasNetLicitacaoScraper;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;
import com.effecti.licitacoes.scraper.ItensEditalScraper; // Importação do scraper de itens
import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ILicitacaoScraperFactory {

    private final IHttpClient httpClient;
    private final ItensEditalScraper itensEditalScraper;

    @Autowired
    @Lazy
    private LicitacaoService licitacaoService;

    @Autowired
    public ILicitacaoScraperFactory(IHttpClient httpClient, ItensEditalScraper itensEditalScraper) {
        this.httpClient = httpClient;
        this.itensEditalScraper = itensEditalScraper;
    }

    public ILicitacaoScraper createScraper() {
        return new ComprasNetLicitacaoScraper(httpClient, itensEditalScraper, licitacaoService);
    }
}
