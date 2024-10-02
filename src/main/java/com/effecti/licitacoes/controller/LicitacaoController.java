package com.effecti.licitacoes.controller;

import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/licitacoes")
public class LicitacaoController {

    @Autowired
    private LicitacaoService licitacaoService;

    @GetMapping()
    public Page<Licitacao> listarLicitacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return licitacaoService.buscarLicitacoes(page, size);
    }


}
