package com.br.pdvfrontend.model;

public class Acesso {

    private Long id;
    private String usuario;
    private String senha;

    public Acesso() {}

    public Acesso(Long id, String usuario, String senha) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }

    public void setId(Long id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setSenha(String senha) { this.senha = senha; }
}
