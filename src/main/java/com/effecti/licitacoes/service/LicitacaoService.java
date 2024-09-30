package com.effecti.licitacoes.service;

import com.effecti.licitacoes.factory.ILicitacaoScraperFactory;
import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.repository.LicitacaoRepository;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicitacaoService {

    @Autowired
    private LicitacaoRepository licitacaoRepository;

    private final ILicitacaoScraperFactory scraperFactory;

    @Autowired
    public LicitacaoService(ILicitacaoScraperFactory scraperFactory) {
        this.scraperFactory = scraperFactory;
    }


    public void buscarNovasLicitacoes(String paginaOrigem) {
        ILicitacaoScraper licitacaoScraper = scraperFactory.createScraper();
        List<Licitacao> licitacoes = licitacaoScraper.buscarLicitacoes(paginaOrigem);
        salvarLicitacoes(licitacoes);
    }

    public void salvarLicitacoes(List<Licitacao> licitacoes) {
        for (Licitacao licitacao : licitacoes) {
            if (!licitacaoRepository.existsByNumeroPregao(licitacao.getNumeroPregao())) {
                licitacaoRepository.save(licitacao);
                System.out.println("Licitacao salva: " + licitacao.getNumeroPregao());
            }
        }

    }

}