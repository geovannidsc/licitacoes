package com.effecti.licitacoes.controller;

import com.effecti.licitacoes.model.Licitacao;
import com.effecti.licitacoes.service.LicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/licitacoes")
public class LicitacaoController {

    @Autowired
    private LicitacaoService licitacaoService;

    @GetMapping
    public ResponseEntity<Page<Licitacao>> buscarLicitacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String termo) {

        Page<Licitacao> licitacaos = licitacaoService.buscarLicitacoes(page, size, termo);
        return ResponseEntity.ok().body(licitacaos);
    }


    @PutMapping("/alternar-leitura")
    public ResponseEntity<String> alternarLicitacaoLida(
            @RequestParam String numeroPregao,
            @RequestParam String codigoUasg) {

        try {
            boolean isLida = licitacaoService.marcarLeitura(numeroPregao, codigoUasg);

            if (isLida) {
                return ResponseEntity.ok("Licitação marcada como lida.");
            } else {
                return ResponseEntity.ok("Licitação desmarcada como lida.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao alternar status da licitação: " + e.getMessage());
        }
    }

}
