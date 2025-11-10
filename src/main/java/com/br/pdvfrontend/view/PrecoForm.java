package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.PrecoService;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class PrecoForm extends JDialog {

    private final PrecoList ownerList;
    private final PrecoService precoService;
    private final ProdutoService produtoService = new ProdutoService();
    private Preco preco;

    private JComboBox<Produto> produtoCombo;
    private JTextField valorField;

    public PrecoForm(Frame owner, PrecoList ownerList, PrecoService precoService, Preco preco) {
        super(owner, (preco == null) ? "Novo Preço" : "Editar Preço", true);
        this.ownerList = ownerList;
        this.precoService = precoService;
        this.preco = preco;

        setSize(450, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Produto
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        produtoCombo = new JComboBox<>();
        for (Produto p : produtoService.listar()) produtoCombo.addItem(p);
        panel.add(produtoCombo, gbc);

        // Valor Venda
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Valor Venda:"), gbc);
        gbc.gridx = 1;
        valorField = new JTextField(15);
        panel.add(valorField, gbc);

        add(panel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setIcon(new FlatSVGIcon("icons/save.svg"));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new FlatSVGIcon("icons/cancel.svg"));
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        if (preco != null) {
            valorField.setText(preco.getValor().toString());
            Produto atual = produtoService.buscarPorId(preco.getProdutoId());
            produtoCombo.setSelectedItem(atual);
        }
    }

    private void onSalvar() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText());

            Produto produtoSelecionado = (Produto) produtoCombo.getSelectedItem();
            if (produtoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!");
                return;
            }

            if (preco == null) {
                preco = new Preco();
            }

            preco.setProdutoId(produtoSelecionado.getId());
            preco.setValor(valor);

            precoService.salvar(preco);

            ownerList.atualizarTabela();
            JOptionPane.showMessageDialog(this, "Preço salvo com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar preço: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
