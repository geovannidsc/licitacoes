package com.effecti.licitacoes.scraper;

import com.effecti.licitacoes.http.IHttpClient;
import com.effecti.licitacoes.model.ItemEdital;
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

    private final ItensEditalScraper itensEditalScraper;

    public ComprasNetLicitacaoScraper(IHttpClient httpClient, ItensEditalScraper itensEditalScraper) {
        this.httpClient = httpClient;
        this.itensEditalScraper = itensEditalScraper;
    }


    @Override
    public List<Licitacao> buscarLicitacoes(String urlBase) {
        List<Licitacao> licitacoes = new ArrayList<>();
        int paginaAtual = 1;
        boolean continuarPaginar = true;

        try {
            Document primeiraPagina = httpClient.getDocument(urlBase + 1);
            String conteudoPrimeiraPagina = primeiraPagina.body().text();

            while (continuarPaginar) {
                String urlPaginaAtual = urlBase + paginaAtual;
                Document doc = httpClient.getDocument(urlPaginaAtual);
                String conteudoPaginaAtual = doc.body().text();

                if (paginaAtual > 1 && conteudoPaginaAtual.equals(conteudoPrimeiraPagina)) {
                    System.out.println("Página " + paginaAtual + " retornou ao conteúdo da primeira página. Parando a paginação.");
                    break;
                }

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



    private List<Licitacao> processarLicitacoes(Document doc) throws Exception {
        List<Licitacao> licitacoes = new ArrayList<>();
        Elements rows = doc.select("table tr");

        for (Element row : rows) {
            Elements boldElements = row.select("b");

            if (boldElements.size() >= 3) {
                Licitacao licitacao = new Licitacao();
                analisarElementosEmNegrito(boldElements, licitacao);
                licitacao.setNumeroPregao(extrairNumeroPregao(licitacao.getPregao()));


                String urlItens = gerarUrlItens(licitacao.getCodigoUasg(), licitacao.getNumeroPregao(), 5);
                List<ItemEdital> itens = capturarItensEdital(urlItens, licitacao);
                licitacao.setItensEdital(itens);
                licitacoes.add(licitacao);
            }
        }

        return licitacoes;
    }

    private List<ItemEdital> capturarItensEdital(String url, Licitacao licitacao) throws Exception {
        List<ItemEdital> itens = new ArrayList<>();
        Document document = httpClient.getDocument(url);
      //  System.out.println("Capturando itens de: " + url);

        if (document == null) {
            System.err.println("Documento retornado é nulo para a URL: " + url);
            return itens;
        }

        itens = itensEditalScraper.capturarItensEdital(document, licitacao);

        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado. Tentando com modprp=3...");
            String urlModprp3 = gerarUrlItens(licitacao.getCodigoUasg(), licitacao.getNumeroPregao(), 3);
            Document documentModprp3 = httpClient.getDocument(urlModprp3);

            if (documentModprp3 != null) {
              //  System.out.println("Capturando itens de (modprp=3): " + urlModprp3);
                itens = itensEditalScraper.capturarItensEdital(documentModprp3, licitacao);
            } else {
                System.err.println("Documento retornado é nulo para a URL: " + urlModprp3);
            }
        }

        return itens;
    }

    private String gerarUrlItens(String codigoUasg, String numeroPregao, int modprp) {
        String numeroPregaoSemBarra = numeroPregao.replace("/", "");
        return String.format("http://comprasnet.gov.br/ConsultaLicitacoes/download/download_editais_detalhe.asp?coduasg=%s&modprp=%d&numprp=%s", codigoUasg, modprp, numeroPregaoSemBarra);
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
            return proximoIrmao.replace("&nbsp;", "").trim();
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
