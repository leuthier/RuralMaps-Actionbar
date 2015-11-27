package com.mpoo.ruralmaps.ruralmaps;

public class Usuario {
    private Integer _id;
    private String nome;
    private String login;
    private String senha;
    private String email;

    public Usuario(){}

    public Usuario (Integer id, String login, String senha){
        this._id = id;
        this.login = login;
        this.senha = senha;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}

