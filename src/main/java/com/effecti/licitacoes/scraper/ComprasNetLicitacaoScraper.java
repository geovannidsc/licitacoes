package com.effecti.licitacoes.scraper;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.model.Licitacao;
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
    public List<Licitacao> buscarLicitacoes(String urlBase) {
        List<Licitacao> licitacoes = new ArrayList<>();
        int paginaAtual = 1;
        boolean continuarPaginar = true;

        try {
            // Obter conteúdo da página 1 para comparações futuras
            Document primeiraPagina = httpClient.getDocument(urlBase + 1);
            String conteudoPrimeiraPagina = primeiraPagina.body().text(); // Extrair o texto da primeira página

            while (continuarPaginar) {
                // Gerar a URL para a página atual
                String urlPaginaAtual = urlBase + paginaAtual;
                Document doc = httpClient.getDocument(urlPaginaAtual);
                String conteudoPaginaAtual = doc.body().text();

                // Verificar se o conteúdo da página atual é o mesmo que o da primeira página
                if (paginaAtual > 1 && conteudoPaginaAtual.equals(conteudoPrimeiraPagina)) {
                    System.out.println("Página " + paginaAtual + " retornou ao conteúdo da primeira página. Parando a paginação.");
                    break; // Interrompe o loop se a página atual for igual à primeira
                }

                // Processar as licitações da página atual
                List<Licitacao> licitacoesPagina = processarLicitacoes(doc);
                licitacoes.addAll(licitacoesPagina);

                System.out.println("Página " + paginaAtual + " processada.");
                paginaAtual++;
            }
        } catch (Exception e) {
            System.err.println("Erro ao capturar licitações: " + e.getMessage());
        }
        return licitacoes;
    }

    private List<Licitacao> processarLicitacoes(Document doc) {
        List<Licitacao> licitacoes = new ArrayList<>();
        Elements rows = doc.select("table tr");

        for (Element row : rows) {
            Elements boldElements = row.select("b");

            if (boldElements.size() >= 3) {
                Licitacao licitacao = new Licitacao();
                analisarElementosEmNegrito(boldElements, licitacao);
                licitacao.setNumeroPregao(extrairNumeroPregao(licitacao.getPregao()));
                licitacoes.add(licitacao);
            }
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
        Pattern pattern = Pattern.compile("(\\d{1,5}/\\d{4})");
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
