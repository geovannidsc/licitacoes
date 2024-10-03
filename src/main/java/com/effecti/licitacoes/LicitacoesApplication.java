package com.effecti.licitacoes;

import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class LicitacoesApplication implements CommandLineRunner {

    @Autowired
    private LicitacaoService licitacaoService;

    public static void main(String[] args) {
        SpringApplication.run(LicitacoesApplication.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            licitacaoService.buscarNovasLicitacoes(licitacaoService.gerarUrlCapturaLicitacoes());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
