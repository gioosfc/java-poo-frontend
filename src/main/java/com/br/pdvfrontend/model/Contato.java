package com.br.pdvfrontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contato {
    //atributos
    private Long id;
    private String telefone;
    private String endereco;
    private  String email;

    //construtores
    public Contato() {
        // Construtor vazio necessário para a criação de novas instâncias no formulário
    }

    public Contato (String telefone, String email, String endereco){
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    //getters
    public Long getId() {
        return id;
    }

    public String getTelefone (){
        return telefone;
    }

    public String getEmail (){
        return email;
    }

    public String getEndereco (){
        return endereco;
    }

    //setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
