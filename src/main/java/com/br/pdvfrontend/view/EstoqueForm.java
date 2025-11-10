package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.EstoqueService;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EstoqueForm extends JDialog {

    private final EstoqueList ownerList;
    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    private Estoque estoque;

    private JComboBox<ProdutoItem> cbProduto;
    private JSpinner spQuantidade;

    public EstoqueForm(Frame owner, EstoqueList ownerList, EstoqueService estoqueService, ProdutoService produtoService, Estoque estoque) {
        super(owner, (estoque == null ? "Novo Estoque" : "Editar Estoque"), true);
        this.ownerList = ownerList;
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
        this.estoque = estoque;

        setSize(450, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Produto
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        cbProduto = new JComboBox<>();
        carregarProdutos();
        form.add(cbProduto, gbc);

        // Quantidade
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        spQuantidade = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 9999999.99, 0.50));
        form.add(spQuantidade, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setIcon(new FlatSVGIcon("icons/save.svg"));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new FlatSVGIcon("icons/cancel.svg"));
        buttons.add(btnSalvar);
        buttons.add(btnCancelar);

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        if (estoque != null) {
            carregarDados();
        }
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoService.listar();
        cbProduto.removeAllItems();
        for (Produto p : produtos) {
            cbProduto.addItem(new ProdutoItem(p.getId(), p.getNome(), p.getReferencia()));
        }
    }

    private void carregarDados() {
        if (estoque.getProdutoId() != null) {
            for (int i = 0; i < cbProduto.getItemCount(); i++) {
                ProdutoItem it = cbProduto.getItemAt(i);
                if (it.id.equals(estoque.getProdutoId())) {
                    cbProduto.setSelectedIndex(i);
                    break;
                }
            }
        }
        if (estoque.getQuantidade() != null) {
            spQuantidade.setValue(estoque.getQuantidade().doubleValue());
        }
    }

    private void onSalvar() {
        ProdutoItem item = (ProdutoItem) cbProduto.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double qtdDouble = (double) spQuantidade.getValue();
        BigDecimal qtd = BigDecimal.valueOf(qtdDouble);

        if (estoque == null) estoque = new Estoque();
        estoque.setProdutoId(item.id);
        estoque.setQuantidade(qtd);

        Estoque salvo = estoqueService.salvar(estoque);
        if (salvo == null) {
            JOptionPane.showMessageDialog(this, "Não foi possível salvar. Verifique se já existe estoque para esse produto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ownerList.atualizarTabela();
        JOptionPane.showMessageDialog(this, "Estoque salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private static class ProdutoItem {
        Long id;
        String nome;
        String referencia;

        ProdutoItem(Long id, String nome, String referencia) {
            this.id = id;
            this.nome = nome;
            this.referencia = referencia;
        }

        @Override
        public String toString() {
            return nome + " (" + referencia + ")";
        }
    }
}
