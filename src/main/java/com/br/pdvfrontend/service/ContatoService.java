package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Contato;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de serviço para gerenciar a lógica de negócio da entidade Contato.
 * Por enquanto, opera com uma lista em memória.
 */
public class ContatoService {

    private final List<Contato> listaDeContatos = new ArrayList<>();

    public ContatoService() {
        // Adiciona dados de exemplo quando o serviço é instanciado
        listaDeContatos.add(new Contato("(48) 99999-8888", "joao.silva@email.com", "Rua das Flores, 123"));
        listaDeContatos.add(new Contato("(11) 55555-4444", "maria.santos@email.com", "Avenida Paulista, 1000"));
    }

    /**
     * Retorna a lista de todos os contatos.
     * @return uma lista de Contato.
     */
    public List<Contato> listarTodos() {
        return new ArrayList<>(listaDeContatos); // Retorna uma cópia para proteger a lista original
    }

    /**
     * Salva um novo contato ou atualiza um existente.
     * @param contato O objeto Contato a ser salvo.
     */
    public void salvar(Contato contato) {
        // Como não temos um ID, a lógica de atualização é mais simples.
        // Se o objeto não estiver na lista, ele é adicionado.
        if (!listaDeContatos.contains(contato)) {
            listaDeContatos.add(contato);
        }
        // Se já existir, os dados já foram alterados no objeto de referência antes de chamar este método.
    }

    /**
     * Exclui um contato da lista.
     * @param contato O objeto Contato a ser excluído.
     */
    public void excluir(Contato contato) {
        listaDeContatos.remove(contato);
    }
}
