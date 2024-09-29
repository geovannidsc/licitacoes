package com.effecti.licitacoes.service;

import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;

import java.util.List;

public class LicitacaoService {

    private final ILicitacaoScraper scraper;

    public LicitacaoService(ILicitacaoScraper scraper) {
        this.scraper = scraper;
    }

    public List<Licitacao> getLicitacoes(String source) {
        return scraper.buscarLicitacoes(source);
    }

    public void marcarComoLida(Licitacao licitacao) {
        licitacao.setLida(true);
    }
}