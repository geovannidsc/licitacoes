package com.effecti.licitacoes.model;

import java.time.LocalDate;
import java.util.Objects;

public class Licitacao {

    private String orgaoResponsavel;
    private String setorSolicitante;
    private String pregao;
    private String objeto;
    private String telefone;
    private String endereco;
    private String editalAPartirDe;
    private String entregaProposta;
    private LocalDate dataEntregaProposta;
    private String codigoUasg;
    private boolean lida;

    public Licitacao() {
    }


    public Licitacao(String orgaoResponsavel, String setorSolicitante, String pregao, String objeto, String telefone, String endereco, String editalAPartirDe, String entregaProposta, LocalDate dataEntregaProposta, String codigoUasg, boolean lida) {
        this.orgaoResponsavel = orgaoResponsavel;
        this.setorSolicitante = setorSolicitante;
        this.pregao = pregao;
        this.objeto = objeto;
        this.telefone = telefone;
        this.endereco = endereco;
        this.editalAPartirDe = editalAPartirDe;
        this.entregaProposta = entregaProposta;
        this.dataEntregaProposta = dataEntregaProposta;
        this.codigoUasg = codigoUasg;
        this.lida = lida;
    }


    public String getOrgaoResponsavel() {
        return orgaoResponsavel;
    }

    public void setOrgaoResponsavel(String orgaoResponsavel) {
        this.orgaoResponsavel = orgaoResponsavel;
    }

    public String getSetorSolicitante() {
        return setorSolicitante;
    }

    public void setSetorSolicitante(String setorSolicitante) {
        this.setorSolicitante = setorSolicitante;
    }

    public String getPregao() {
        return pregao;
    }

    public void setPregao(String pregao) {
        this.pregao = pregao;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEditalAPartirDe() {
        return editalAPartirDe;
    }

    public void setEditalAPartirDe(String editalAPartirDe) {
        this.editalAPartirDe = editalAPartirDe;
    }

    public String getEntregaProposta() {
        return entregaProposta;
    }

    public void setEntregaProposta(String entregaProposta) {
        this.entregaProposta = entregaProposta;
    }

    public LocalDate getDataEntregaProposta() {
        return dataEntregaProposta;
    }

    public void setDataEntregaProposta(LocalDate dataEntregaProposta) {
        this.dataEntregaProposta = dataEntregaProposta;
    }

    public String getCodigoUasg() {
        return codigoUasg;
    }

    public void setCodigoUasg(String codigoUasg) {
        this.codigoUasg = codigoUasg;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Licitacao licitacao = (Licitacao) o;
        return Objects.equals(orgaoResponsavel, licitacao.orgaoResponsavel) && Objects.equals(entregaProposta, licitacao.entregaProposta) && Objects.equals(codigoUasg, licitacao.codigoUasg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgaoResponsavel, entregaProposta, codigoUasg);
    }

    @Override
    public String toString() {
        return "Licitacao {\n" +
                " Órgão: '" + orgaoResponsavel + "'\n" +
                " Setor: '" + setorSolicitante + "'\n" +
                " Código da UASG: '" + codigoUasg + "'\n" +
                " Pregão: '" + pregao + "'\n" +
                " Objeto: '" + objeto + "'\n" +
                " Telefone: '" + telefone + "'\n" +
                " Endereço: '" + endereco + "'\n" +
                " Edital a partir de: '" + editalAPartirDe + "'\n" +
                " Entrega da Proposta: '" + entregaProposta + "'\n" +
                '}';
    }


}