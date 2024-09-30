package com.effecti.licitacoes.factory;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.scraper.ComprasNetLicitacaoScraper;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ILicitacaoScraperFactory {

    private final IHttpClient httpClient;

    @Autowired
    public ILicitacaoScraperFactory(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ILicitacaoScraper createScraper() {
        return new ComprasNetLicitacaoScraper(httpClient);
    }
}