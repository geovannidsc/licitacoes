package com.effecti.licitacoes.controller;

import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/licitacoes")
public class LicitacaoController {

    @Autowired
    private LicitacaoService licitacaoService;

    @GetMapping
    public ResponseEntity<List<Licitacao>> buscarTodos() {
        List<Licitacao> list = licitacaoService.buscarLicitacoes();
        return ResponseEntity.ok().body(list);
    }





}
