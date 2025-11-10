package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.CustoService;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class CustoForm extends JDialog {

    private final CustoList ownerList;
    private final CustoService custoService;
    private final ProdutoService produtoService = new ProdutoService();
    private Custos custo;

    private JComboBox<Produto> produtoCombo;
    private JTextField impostoField;
    private JTextField custoVariaveisField;
    private JTextField margemLucroField;
    private JTextField custoFixoField;

    public CustoForm(Frame owner, CustoList ownerList, CustoService custoService, Custos custo) {
        super(owner, (custo == null) ? "Novo Custo" : "Editar Custo", true);
        this.ownerList = ownerList;
        this.custoService = custoService;
        this.custo = custo;

        setSize(450, 400);
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
        for (Produto p : produtoService.listar()) {
            produtoCombo.addItem(p);
        }
        panel.add(produtoCombo, gbc);

        // Imposto
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Imposto:"), gbc);
        gbc.gridx = 1;
        impostoField = new JTextField(15);
        panel.add(impostoField, gbc);

        // Custo Variável
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Custo Variável:"), gbc);
        gbc.gridx = 1;
        custoVariaveisField = new JTextField(15);
        panel.add(custoVariaveisField, gbc);

        // Margem de Lucro
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Margem de Lucro:"), gbc);
        gbc.gridx = 1;
        margemLucroField = new JTextField(15);
        panel.add(margemLucroField, gbc);

        // Custo Fixo
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Custo Fixo:"), gbc);
        gbc.gridx = 1;
        custoFixoField = new JTextField(15);
        panel.add(custoFixoField, gbc);

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

        if (custo != null) {
            impostoField.setText(String.valueOf(custo.getImposto()));
            custoVariaveisField.setText(String.valueOf(custo.getCustoVariaveis()));
            margemLucroField.setText(String.valueOf(custo.getMargemLucro()));
            custoFixoField.setText(String.valueOf(custo.getCustoFixo()));

            Produto atual = produtoService.buscarPorId(custo.getProdutoId());
            produtoCombo.setSelectedItem(atual);
            produtoCombo.setEnabled(false);
        }
    }

    private void onSalvar() {
        try {
            Produto produtoSelecionado = (Produto) produtoCombo.getSelectedItem();
            if (produtoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!");
                return;
            }

            double imposto = Double.parseDouble(impostoField.getText());
            double custoVariaveis = Double.parseDouble(custoVariaveisField.getText());
            double margemLucro = Double.parseDouble(margemLucroField.getText());
            double custoFixo = Double.parseDouble(custoFixoField.getText());

            Custos obj = (custo != null ? custo : new Custos());
            obj.setProdutoId(produtoSelecionado.getId());
            obj.setImposto(imposto);
            obj.setCustoVariaveis(custoVariaveis);
            obj.setMargemLucro(margemLucro);
            obj.setCustoFixo(custoFixo);

            if (obj.getDataProcessamento() == null) {
                obj.setDataProcessamento(new Date());
            }

            custoService.salvar(obj);
            ownerList.atualizarTabela();
            JOptionPane.showMessageDialog(this, "Custo salvo com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar!");
            ex.printStackTrace();
        }
    }
}
