package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class ProdutoForm extends JDialog {

    private JTextField txtNome, txtReferencia, txtMarca, txtCategoria, txtFornecedor;
    private ProdutoService produtoService;
    private Produto produto;
    private ProdutoList parent;

    public ProdutoForm(ProdutoList parent, ProdutoService produtoService, Produto produto) {
        super(parent, (produto == null ? "Novo Produto" : "Editar Produto"), true);
        this.parent = parent;
        this.produtoService = produtoService;
        this.produto = produto;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        panel.add(txtNome, gbc);

        // Referência
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Referência:"), gbc);
        gbc.gridx = 1;
        txtReferencia = new JTextField(20);
        panel.add(txtReferencia, gbc);

        // Marca
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        txtMarca = new JTextField(20);
        panel.add(txtMarca, gbc);

        // Categoria
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        txtCategoria = new JTextField(20);
        panel.add(txtCategoria, gbc);

        // Fornecedor
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1;
        txtFornecedor = new JTextField(20);
        panel.add(txtFornecedor, gbc);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setIcon(new FlatSVGIcon("icons/save.svg"));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new FlatSVGIcon("icons/cancel.svg"));
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        if (produto != null) {
            txtNome.setText(produto.getNome());
            txtReferencia.setText(produto.getReferencia());
            txtMarca.setText(produto.getMarca());
            txtCategoria.setText(produto.getCategoria());
            txtFornecedor.setText(produto.getFornecedor());
        }

        setVisible(true);
    }

    private void salvar() {
        if (produto == null) {
            produto = new Produto();
        }

        produto.setNome(txtNome.getText());
        produto.setReferencia(txtReferencia.getText());
        produto.setMarca(txtMarca.getText());
        produto.setCategoria(txtCategoria.getText());
        produto.setFornecedor(txtFornecedor.getText());

        produtoService.salvar(produto);

        parent.atualizarTabela();
        dispose();
    }
}
