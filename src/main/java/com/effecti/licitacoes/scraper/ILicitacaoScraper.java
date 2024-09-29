package com.effecti.licitacoes.scraper;


import com.effecti.licitacoes.model.Licitacao;

import java.util.List;

public interface ILicitacaoScraper {

    List<Licitacao> buscarLicitacoes(String source);
}