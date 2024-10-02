package com.effecti.licitacoes.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class JsoupHttpClient implements IHttpClient {

    @Override
    public Document getDocument(String url) throws Exception {
        if (url.startsWith("file:///")) {
            Path path = Paths.get(url.substring(8));
            if (Files.exists(path)) {
                try {
                    return Jsoup.parse(path.toFile(), "ISO-8859-1");
                } catch (AccessDeniedException e) {
                    System.err.println("Acesso negado ao arquivo: " + path);
                    throw e; // Re-throw para tratamento superior
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo: " + e.getMessage());
                    throw e;
                }
            } else {
                throw new FileNotFoundException("Arquivo não encontrado: " + path);
            }
        }

        Connection.Response response = Jsoup.connect(url).execute();

        String responseBody = new String(response.bodyAsBytes(), Charset.forName("ISO-8859-1"));

        return Jsoup.parse(responseBody, url);
    }
}
