package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.ProdutoService;
import javax.swing.*;
import java.awt.*;

public class ProdutoForm extends JDialog {

    private JTextField txtNome, txtReferencia, txtMarca, txtCategoria, txtFornecedor;
    private ProdutoService produtoService;
    private Produto produto;
    private ProdutoList parent;

    public ProdutoForm(ProdutoList parent, ProdutoService produtoService, Produto produto) {
        this.parent = parent;
        this.produtoService = produtoService;
        this.produto = produto;

        setTitle(produto == null ? "Novo Produto" : "Editar Produto");
        setSize(400, 350);
        setLayout(new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("Referência:"));
        txtReferencia = new JTextField();
        add(txtReferencia);

        add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        add(txtMarca);

        add(new JLabel("Categoria:"));
        txtCategoria = new JTextField();
        add(txtCategoria);

        add(new JLabel("Fornecedor:"));
        txtFornecedor = new JTextField();
        add(txtFornecedor);

        add(new JLabel()); // Espaçador
        add(new JLabel()); // Espaçador

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        if (produto != null) {
            txtNome.setText(produto.getNome());
            txtReferencia.setText(produto.getReferencia());
            txtMarca.setText(produto.getMarca());
            txtCategoria.setText(produto.getCategoria());
            txtFornecedor.setText(produto.getFornecedor());
        }

        setModal(true);
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