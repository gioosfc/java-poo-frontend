package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.ProdutoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoList extends JFrame {
    private ProdutoService produtoService = new ProdutoService();
    private JTable table;

    public ProdutoList() {
        setTitle("Cadastro de Produtos");
        setSize(800, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        btnNovo.addActionListener(e -> new ProdutoForm(null, this));
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());

        JPanel botoes = new JPanel();
        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);

        table = new JTable();
        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        atualizarTabela();
        setVisible(true);
    }

    public void atualizarTabela() {
        List<Produto> produtos = produtoService.listar();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome", "Referência", "Marca", "Categoria", "Fornecedor"}, 0);
        for (Produto p : produtos) {
            model.addRow(new Object[]{p.getId(), p.getNome(), p.getReferencia(), p.getMarca(), p.getCategoria(), p.getFornecedor()});
        }
        table.setModel(model);
    }

    private void editarProduto() {
        int linha = table.getSelectedRow();
        if (linha != -1) {
            Long id = (Long) table.getValueAt(linha, 0);
            Produto produto = produtoService.buscarPorId(id);
            new ProdutoForm(produto, this);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }

    private void excluirProduto() {
        int linha = table.getSelectedRow();
        if (linha != -1) {
            Long id = (Long) table.getValueAt(linha, 0);
            produtoService.deletar(id);
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }
}
