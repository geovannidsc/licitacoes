package com.effecti.licitacoes.repository;

import com.effecti.licitacoes.model.Licitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface LicitacaoRepository extends JpaRepository<Licitacao, Long> {


    boolean existsByNumeroPregaoAndCodigoUasg(String numeroPregao, String codigoUasg);

    Page<Licitacao> findAll(Pageable pageable);

}
