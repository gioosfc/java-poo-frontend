package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoList extends JFrame {
    private ProdutoService produtoService = new ProdutoService();
    private JTable table;
    private DefaultTableModel model;

    public ProdutoList() {
        setTitle("Cadastro de Produtos");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Gerenciamento de Produtos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Nome", "Referência", "Marca", "Categoria", "Fornecedor"}, 0);
        table = new JTable(model);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        btnNovo.setIcon(new FlatSVGIcon("icons/add.svg"));
        JButton btnEditar = new JButton("Editar");
        btnEditar.setIcon(new FlatSVGIcon("icons/edit.svg"));
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setIcon(new FlatSVGIcon("icons/delete.svg"));
        btnExcluir.putClientProperty("JButton.buttonType", "toolBarButton");

        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        mainPanel.add(botoes, BorderLayout.SOUTH);

        add(mainPanel);

        btnNovo.addActionListener(e -> new ProdutoForm(this, produtoService, null));
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());

        atualizarTabela();
    }

    public void atualizarTabela() {
        model.setRowCount(0);
        List<Produto> produtos = produtoService.listar();
        for (Produto p : produtos) {
            model.addRow(new Object[]{p.getId(), p.getNome(), p.getReferencia(), p.getMarca(), p.getCategoria(), p.getFornecedor()});
        }
    }

    private void editarProduto() {
        int linha = table.getSelectedRow();
        if (linha != -1) {
            Long id = (Long) table.getValueAt(linha, 0);
            Produto produto = produtoService.buscarPorId(id);
            new ProdutoForm(this, produtoService, produto);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
        }
    }

    private void excluirProduto() {
        int linha = table.getSelectedRow();
        if (linha != -1) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir este produto?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                Long id = (Long) table.getValueAt(linha, 0);
                produtoService.deletar(id);
                atualizarTabela();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
        }
    }
}
