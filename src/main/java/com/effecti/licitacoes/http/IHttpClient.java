package com.effecti.licitacoes.http;


import org.jsoup.nodes.Document;

public interface IHttpClient {
    Document getDocument(String url) throws Exception;
}