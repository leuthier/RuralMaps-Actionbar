package com.mpoo.ruralmaps.ruralmaps;


public class Pessoa {

    private Integer _id;
    private String nome;
    private String email;


    public Pessoa(){}

    public Pessoa(Integer id, String nome, String email){
        this._id = id;
        this.nome = nome;
        this.email = email;
        }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getNome() { return nome;
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
}