package com.effecti.licitacoes.scraper;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.service.LicitacaoService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ComprasNetLicitacaoScraper implements ILicitacaoScraper {

    private final IHttpClient httpClient;


    public ComprasNetLicitacaoScraper(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<Licitacao> buscarLicitacoes(String source) {
        List<Licitacao> licitacoes = new ArrayList<>();

        try {
            Document doc = httpClient.getDocument(source);
            Elements rows = doc.select("table tr");

            for (Element row : rows) {
                Elements boldElements = row.select("b");

                if (boldElements.size() >= 3) {
                    Licitacao licitacao = new Licitacao();
                    analisarElementosEmNegrito(boldElements, licitacao);
                    licitacao.setNumeroPregao(extrairNumeroPregao(licitacao.getPregao()));
                    licitacoes.add(licitacao);
                    //imprimirLicitacao(licitacao);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao capturar licitações: " + e.getMessage());
        }
        return licitacoes;
    }

    private void analisarElementosEmNegrito(Elements boldElements, Licitacao licitacao) {
        capturarOrgaoESetor(boldElements.get(0), licitacao);
        licitacao.setPregao(boldElements.get(1).text());
        licitacao.setObjeto(obterProximoIrmao(boldElements, "Objeto"));
        licitacao.setTelefone(obterProximoIrmao(boldElements, "Telefone"));
        licitacao.setEndereco(obterProximoIrmao(boldElements, "Endereço"));
        licitacao.setEditalAPartirDe(obterProximoIrmao(boldElements, "Edital a partir de"));
        licitacao.setEntregaProposta(obterProximoIrmao(boldElements, "Entrega da Proposta"));
    }

    private void capturarOrgaoESetor(Element primeiroElementoNegrito, Licitacao licitacao) {
        String[] partes = primeiroElementoNegrito.html().split("<br>");
        String orgaoResponsavel = "";
        String setorSolicitante = "";
        String codigoUasg = "Não informado";

        for (String parte : partes) {
            parte = parte.trim();
            if (parte.contains("UASG")) {
                codigoUasg = extrairCodigoUasg(parte);
                break;
            } else if (orgaoResponsavel.isEmpty()) {
                orgaoResponsavel = parte;
            } else {
                setorSolicitante = parte;
            }
        }

        licitacao.setOrgaoResponsavel(orgaoResponsavel);
        licitacao.setSetorSolicitante(setorSolicitante);
        licitacao.setCodigoUasg(codigoUasg);
    }

    private String extrairCodigoUasg(String parte) {
        Pattern pattern = Pattern.compile("\\b\\d{1,6}\\b");
        Matcher matcher = pattern.matcher(parte);
        return matcher.find() ? matcher.group() : "Não informado";
    }

    private String obterProximoIrmao(Elements boldElements, String textoBuscado) {
        Element element = boldElements.stream()
                .filter(el -> el.text().contains(textoBuscado))
                .findFirst()
                .orElse(null);
        if (element != null) {
            String proximoIrmao = element.nextSibling() != null ? element.nextSibling().toString().trim() : "Não informado";
            return proximoIrmao.replace("&nbsp;", "").trim(); // Remover &nbsp; e espaços desnecessários
        }
        return "Não informado";
    }

    private String extrairNumeroPregao(String texto) {
        Pattern pattern = Pattern.compile("(\\d{5}/\\d{4})");
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Não informado";
        }
    }



    private void imprimirLicitacao(Licitacao licitacao) {
        System.out.println(licitacao.toString());
        System.out.println("--------------------");
    }
}
