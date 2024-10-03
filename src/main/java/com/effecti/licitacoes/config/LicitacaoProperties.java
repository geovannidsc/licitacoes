package com.effecti.licitacoes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "licitacoes")
public class LicitacaoProperties {
    private String urlCapturaItens;
    private String urlCapturaLicitacoes;

    public String getUrlCapturaItens() {
        return urlCapturaItens;
    }

    public void setUrlCapturaItens(String urlCapturaItens) {
        this.urlCapturaItens = urlCapturaItens;
    }

    public String getUrlCapturaLicitacoes() {
        return urlCapturaLicitacoes;
    }

    public void setUrlCapturaLicitacoes(String urlCapturaLicitacoes) {
        this.urlCapturaLicitacoes = urlCapturaLicitacoes;
    }
}
