package com.br.pdvfrontend.model;

public class Acesso {

    private Long id;
    private String usuario;
    private String senha;
    private String papel; // ✅ novo campo

    public Acesso() {}

    public Acesso(Long id, String usuario, String senha, String papel) {
        this.id = id;
        this.usuario = usuario;
        this.senha = senha;
        this.papel = papel;
    }

    public Acesso(Object o, String usuario, String senha) {
    }

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getSenha() { return senha; }
    public String getPapel() { return papel; } // ✅ getter novo

    public void setId(Long id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setPapel(String papel) { this.papel = papel; } // ✅ setter novo
}
