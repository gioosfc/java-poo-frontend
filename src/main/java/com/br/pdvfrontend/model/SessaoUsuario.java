package com.br.pdvfrontend.model;


public class SessaoUsuario {

    public static String usuario;
    public static String papel;

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        SessaoUsuario.usuario = usuario;
    }

    public static String getPapel() {
        return papel;
    }

    public static void setPapel(String papel) {
        SessaoUsuario.papel = papel;
    }
}

