package com.effecti.licitacoes.scraper;

import com.effecti.licitacoes.model.ItemEdital;
import com.effecti.licitacoes.model.Licitacao;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItensEditalScraper implements IItensEditalScraper {

    @Override
    public List<ItemEdital> capturarItensEdital(Document document, Licitacao licitacao) {
        List<ItemEdital> itens = new ArrayList<>();
        Elements itemNomeElements = document.select("span.tex3b");

        if (itemNomeElements.isEmpty()) {
            System.err.println("Nenhum item encontrado com o seletor 'span.tex3b'. Verifique a estrutura da página.");
            return itens;
        }

        for (Element itemNomeElement : itemNomeElements) {
            String idNomeTexto = itemNomeElement.text();

            String[] idNomeSplit = idNomeTexto.split(" - ", 2);
            String id = idNomeSplit[0].trim();
            if (!id.matches("\\d+")) {
             //   System.out.println("ID inválido encontrado: " + id);
                continue;
            }

            String nome = idNomeSplit.length > 1 ? idNomeSplit[1].trim() : "Nome não encontrado";

            Element descricaoElement = itemNomeElement.parent().selectFirst("span.tex3");

            StringBuilder descricao = new StringBuilder();

            if (descricaoElement != null) {
                String[] descricaoParts = descricaoElement.html().split("<br>");
                for (String part : descricaoParts) {
                    descricao.append(part.trim()).append(" | ");
                }

                if (descricao.length() > 0) {
                    descricao.setLength(descricao.length() - 3);
                }
            } else {
                descricao.append("Descrição não encontrada");
            }

            ItemEdital item = new ItemEdital(id, nome, descricao.toString(), licitacao);
           // System.out.println(item);
            itens.add(item);
        }

        return itens;
    }
}
