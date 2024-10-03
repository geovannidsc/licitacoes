package com.effecti.licitacoes.service;

import com.effecti.licitacoes.factory.ILicitacaoScraperFactory;
import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.repository.LicitacaoRepository;
import com.effecti.licitacoes.scraper.ILicitacaoScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public Page<Licitacao> buscarLicitacoes(int page, int size, String termo) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if (termo == null || termo.isEmpty()) {
            return licitacaoRepository.findAll(pageRequest);
        }

        return licitacaoRepository.buscarPorTermo(termo, pageRequest);
    }


    public boolean marcarLeitura(String numeroPregao, String codigoUasg) {
        Licitacao licitacao = licitacaoRepository.findByNumeroPregaoAndCodigoUasg(numeroPregao, codigoUasg);
        if (licitacao != null) {
            licitacao.setLida(!licitacao.isLida());
            licitacaoRepository.save(licitacao);
            return licitacao.isLida();
        }
        throw new RuntimeException("Licitação não encontrada.");
    }


    public void salvarLicitacoes(List<Licitacao> licitacoes) {
        for (Licitacao licitacao : licitacoes) {
            if (!licitacaoRepository.existsByNumeroPregaoAndCodigoUasg(licitacao.getNumeroPregao(), licitacao.getCodigoUasg())) {
                licitacaoRepository.save(licitacao);
                System.out.println("Licitacao salva: " + licitacao.getNumeroPregao());
            }
        }

    }

}