package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.CustoService;
import com.br.pdvfrontend.service.ProdutoService;

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

        setSize(400, 350);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        // ---- PRODUTO ----
        panel.add(new JLabel("Produto:"));
        produtoCombo = new JComboBox<>();

        for (Produto p : produtoService.listar()) {
            produtoCombo.addItem(p);
        }

        panel.add(produtoCombo);

        // ---- CAMPOS ----
        panel.add(new JLabel("Imposto:"));
        impostoField = new JTextField();
        panel.add(impostoField);

        panel.add(new JLabel("Custo Variável:"));
        custoVariaveisField = new JTextField();
        panel.add(custoVariaveisField);

        panel.add(new JLabel("Margem de Lucro:"));
        margemLucroField = new JTextField();
        panel.add(margemLucroField);

        panel.add(new JLabel("Custo Fixo:"));
        custoFixoField = new JTextField();
        panel.add(custoFixoField);

        // ---- BOTÕES ----
        JButton salvar = new JButton("Salvar");
        salvar.addActionListener(e -> onSalvar());
        panel.add(salvar);

        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(e -> dispose());
        panel.add(cancelar);

        add(panel);

        // ---- SE FOR EDIÇÃO ----
        if (custo != null) {

            impostoField.setText(String.valueOf(custo.getImposto()));
            custoVariaveisField.setText(String.valueOf(custo.getCustoVariaveis()));
            margemLucroField.setText(String.valueOf(custo.getMargemLucro()));
            custoFixoField.setText(String.valueOf(custo.getCustoFixo()));

            Produto atual = produtoService.buscarPorId(custo.getProdutoId());
            produtoCombo.setSelectedItem(atual);

            // ✅ Bloqueia troca de produto ao editar
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

            // ✅ Reaproveita objeto ao editar
            Custos obj = (custo != null ? custo : new Custos());

            obj.setProdutoId(produtoSelecionado.getId());
            obj.setImposto(imposto);
            obj.setCustoVariaveis(custoVariaveis);
            obj.setMargemLucro(margemLucro);
            obj.setCustoFixo(custoFixo);

            // ✅ Só define a data se for novo
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
