package com.effecti.licitacoes.http;


import org.jsoup.nodes.Document;

import java.io.IOException;

public interface IHttpClient {
    Document getDocument(String url) throws Exception;
}