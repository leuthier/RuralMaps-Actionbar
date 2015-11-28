package com.mpoo.ruralmaps.ruralmaps;


public class Pessoa {

    private Integer _id;
    private String nome;
    private String email;
    private String pts_pesquisados;


    public Pessoa() {
    }

    public Pessoa(Integer id, String nome, String email, String pts_pesquisados) {
        this._id = id;
        this.nome = nome;
        this.email = email;
        this.pts_pesquisados = pts_pesquisados;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPts_pesquisados() {
        return pts_pesquisados;
    }

    public void setPts_pesquisados(String pts_pesquisados) {
        this.pts_pesquisados = pts_pesquisados;
    }
}