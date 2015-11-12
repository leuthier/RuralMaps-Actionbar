package com.mpoo.ruralmaps.ruralmaps;


public class Pessoa {

    private Integer _id;
    private String pessoa;
    private String dt_criacao;
    private String dt_completado;

    //oi eu sou goku

    public Pessoa(){}

    public Pessoa(Integer id, String pessoa, String dtcriacao, String dtcompletado){
        this._id = id;
        this.pessoa = pessoa;
        this.dt_completado = dtcompletado;
        this.dt_criacao = dtcriacao;
        }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getDt_completado() {
        return dt_completado;
    }

    public void setDt_completado(String dt_completado) {
        this.dt_completado = dt_completado;
    }

    public String getDt_criacao() {
        return dt_criacao;
    }

    public void setDt_criacao(String dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

}

//testando gleydson araujo