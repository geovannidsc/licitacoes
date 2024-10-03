package com.effecti.licitacoes.repository;

import com.effecti.licitacoes.model.Licitacao;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface LicitacaoRepository extends JpaRepository<Licitacao, Long> {


    boolean existsByNumeroPregaoAndCodigoUasg(String numeroPregao, String codigoUasg);

    Page<Licitacao> findAll(Pageable pageable);

    Licitacao findByNumeroPregaoAndCodigoUasg(String numeroPregao, String codigoUasg);

    @Query("SELECT l FROM Licitacao l WHERE " +
            "LOWER(l.objeto) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(l.orgaoResponsavel) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(l.editalAPartirDe) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
            "LOWER(l.setorSolicitante) LIKE LOWER(CONCAT('%', :termo, '%'))")

    Page<Licitacao> buscarPorTermo(@Param("termo") String termo, PageRequest pageRequest);
}
