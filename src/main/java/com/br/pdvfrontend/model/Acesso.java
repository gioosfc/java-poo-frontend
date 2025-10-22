package com.br.pdvfrontend.model;

import java.util.UUID;

public class Acesso {
    //atributos
    private String usuario;
    private String senha;

    //construtor
    public Acesso (String usuario, String senha){
        this.usuario = usuario;
        this.senha = senha;
    }

    //getters
    /*
     * Exibir o valor que foi atribuido ao argumento.
     */
    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    //setters
    /*
     * Atribuir um valor a variavel que irá trazer o valor do argumento para o atributo.
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
