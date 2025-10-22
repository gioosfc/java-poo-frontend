package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Pessoa;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class PessoaService {

    private static final List<Pessoa> mockPessoas = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(4);

    static {
        mockPessoas.add(new Pessoa(1L, "João da Silva", "111.111.111-11", "joao.silva@example.com"));
        mockPessoas.add(new Pessoa(2L, "Maria Oliveira", "222.222.222-22", "maria.oliveira@example.com"));
        mockPessoas.add(new Pessoa(3L, "José Santos", "333.333.333-33", "jose.santos@example.com"));
    }

    public List<Pessoa> listar() {
        // Retorna uma cópia para evitar modificações externas na lista original
        return new ArrayList<>(mockPessoas);
    }

    public Pessoa buscarPorId(Long id) {
        return mockPessoas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Pessoa salvar(Pessoa pessoa) {
        if (pessoa.getId() == null) { // Novo cadastro
            pessoa.setId(idCounter.getAndIncrement());
            mockPessoas.add(pessoa);
        } else { // Atualização
            atualizar(pessoa);
        }
        return pessoa;
    }

    public void atualizar(Pessoa pessoa) {
        for (int i = 0; i < mockPessoas.size(); i++) {
            if (Objects.equals(mockPessoas.get(i).getId(), pessoa.getId())) {
                mockPessoas.set(i, pessoa);
                return;
            }
        }
    }

    public void deletar(Long id) {
        mockPessoas.removeIf(p -> p.getId().equals(id));
    }
}
