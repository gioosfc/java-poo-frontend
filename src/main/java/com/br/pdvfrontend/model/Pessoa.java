package com.br.pdvfrontend.model;

import com.br.pdvfrontend.enums.TipoPessoa;

import java.time.LocalDate;

public class Pessoa {
    private Long id;
    private String nomeCompleto;
    private Long numeroCtps;
    private String cpfCnpj;
    private String email;
    private LocalDate dataNascimento;
    private TipoPessoa tipoPessoa;

    public Pessoa() {
    }

    public Pessoa(String nomeCompleto,
                  String cpfCnpj,
                  Long numeroCtps,
                  LocalDate dataNascimento,
                  TipoPessoa tipoPessoa) {
        this.nomeCompleto = nomeCompleto;
        this.cpfCnpj = cpfCnpj;
        this.numeroCtps = numeroCtps;
        this.dataNascimento = dataNascimento;
        this.tipoPessoa = tipoPessoa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nome) { this.nomeCompleto = nome; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpf) { this.cpfCnpj = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getNumeroCtps() { return numeroCtps; }
    public void setNumeroCtps(Long numeroCtps) { this.numeroCtps = numeroCtps; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public TipoPessoa getTipoPessoa() { return tipoPessoa; }
    public void setTipoPessoa(TipoPessoa tipoPessoa) { this.tipoPessoa = tipoPessoa; }
}
