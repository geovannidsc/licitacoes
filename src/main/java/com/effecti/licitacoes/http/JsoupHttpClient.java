package com.effecti.licitacoes.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class JsoupHttpClient implements IHttpClient {


    @Override
    public Document getDocument(String url) throws Exception {
        // Verifica se o URL é um caminho de arquivo local
        if (url.startsWith("file:///")) {
            // Remove o prefixo "file:///" e cria um objeto Path
            Path path = Paths.get(url.substring(8)); // usa substring(8) para remover 'file:///'
            // Verifica se o arquivo existe
            if (Files.exists(path)) {
                try {
                    return Jsoup.parse(path.toFile(), "ISO-8859-1");
                } catch (AccessDeniedException e) {
                    System.err.println("Acesso negado ao arquivo: " + path.toString());
                    throw e; // Re-throw para tratamento superior
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo: " + e.getMessage());
                    throw e; // Re-throw para tratamento superior
                }
            } else {
                throw new FileNotFoundException("Arquivo não encontrado: " + path.toString());
            }
        }
        // Conecta ao URL fornecido e obtém o documento HTML
        return Jsoup.connect(url).get();
    }

}