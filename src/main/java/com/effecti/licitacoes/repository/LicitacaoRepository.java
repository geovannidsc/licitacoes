package com.effecti.licitacoes.repository;

import com.effecti.licitacoes.model.Licitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicitacaoRepository extends JpaRepository<Licitacao, Long> {

    boolean existsByNumeroPregao(String numeroPregao);

    boolean existsByNumeroPregaoAndCodigoUasg(String numeroPregao, String codigoUasg);

}
