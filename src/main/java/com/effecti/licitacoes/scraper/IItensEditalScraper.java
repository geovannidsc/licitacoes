package com.effecti.licitacoes.scraper;


import com.effecti.licitacoes.model.ItemEdital;
import com.effecti.licitacoes.model.Licitacao;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IItensEditalScraper {

    List<ItemEdital> capturarItensEdital(Document document, Licitacao licitacao);

}
