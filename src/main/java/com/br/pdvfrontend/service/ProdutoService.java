package com.br.pdvfrontend.service;

import com.br.pdvfrontend.model.Produto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class ProdutoService {

    private static final List<Produto> mockProdutos = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong(4);

    static {
        mockProdutos.add(new Produto(1L, "Notebook Gamer", "NTK-GMR-001", "Marca X", "Informática", "Fornecedor A"));
        mockProdutos.add(new Produto(2L, "Mouse Sem Fio", "MSE-SF-345", "Marca Y", "Periféricos", "Fornecedor B"));
        mockProdutos.add(new Produto(3L, "Teclado Mecânico", "TCL-MEC-RGB", "Marca Z", "Periféricos", "Fornecedor A"));
    }

    public List<Produto> listar() {
        return new ArrayList<>(mockProdutos);
    }

    public Produto buscarPorId(Long id) {
        return mockProdutos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Produto salvar(Produto produto) {
        if (produto.getId() == null) { // Novo cadastro
            produto.setId(idCounter.getAndIncrement());
            mockProdutos.add(produto);
        } else { // Atualização
            atualizar(produto);
        }
        return produto;
    }

    public void atualizar(Produto produto) {
        for (int i = 0; i < mockProdutos.size(); i++) {
            if (Objects.equals(mockProdutos.get(i).getId(), produto.getId())) {
                mockProdutos.set(i, produto);
                return;
            }
        }
    }

    public void deletar(Long id) {
        mockProdutos.removeIf(p -> p.getId().equals(id));
    }
}
