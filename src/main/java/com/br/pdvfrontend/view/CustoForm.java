package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.CustoService;
import com.br.pdvfrontend.service.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class CustoForm extends JDialog {

    private final CustoList ownerList;
    private final CustoService custoService;
    private final ProdutoService produtoService = new ProdutoService();
    private Custos custo;

    private JTextField impostoField;
    private JTextField custoVariaveisField;
    private JTextField margemLucroField;
    private JTextField custoFixoField;

    private JComboBox<Produto> comboProduto;

    public CustoForm(Frame owner, CustoList ownerList, CustoService custoService, Custos custo) {
        super(owner, (custo == null) ? "Novo Custo" : "Editar Custo", true);
        this.ownerList = ownerList;
        this.custoService = custoService;
        this.custo = custo;

        setTitle("Cadastro de Custo");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Produto
        panel.add(new JLabel("Produto:"));
        comboProduto = new JComboBox<>();
        carregarProdutos();
        panel.add(comboProduto);

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

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnSalvar);
        panel.add(btnCancelar);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);

        // Preenchimento se estiver editando
        if (custo != null) {
            selecionarProduto(custo.getProdutoId());
            impostoField.setText(String.valueOf(custo.getImposto()));
            custoVariaveisField.setText(String.valueOf(custo.getCustoVariaveis()));
            margemLucroField.setText(String.valueOf(custo.getMargemLucro()));
            custoFixoField.setText(String.valueOf(custo.getCustoFixo()));
        }
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoService.listar();

        for (Produto p : produtos) {
            comboProduto.addItem(p);
        }
    }

    private void selecionarProduto(Long id) {
        if (id == null) return;

        for (int i = 0; i < comboProduto.getItemCount(); i++) {
            Produto p = comboProduto.getItemAt(i);
            if (p.getId().equals(id)) {
                comboProduto.setSelectedIndex(i);
                break;
            }
        }
    }

    private void onSalvar() {
        try {
            Produto produtoSelecionado = (Produto) comboProduto.getSelectedItem();

            if (produtoSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double imposto = Double.parseDouble(impostoField.getText());
            double custoVariaveis = Double.parseDouble(custoVariaveisField.getText());
            double margemLucro = Double.parseDouble(margemLucroField.getText());
            double custoFixo = Double.parseDouble(custoFixoField.getText());
            Date dataProcessamento = new Date();

            if (custo == null) {
                custo = new Custos();
            }

            custo.setImposto(imposto);
            custo.setCustoVariaveis(custoVariaveis);
            custo.setMargemLucro(margemLucro);
            custo.setCustoFixo(custoFixo);
            custo.setDataProcessamento(dataProcessamento);
            custo.setProdutoId(produtoSelecionado.getId()); // ✅ ESSA LINHA RESOLVE O ERRO

            custoService.salvar(custo);

            ownerList.atualizarTabela();

            JOptionPane.showMessageDialog(this, "Custo salvo com sucesso!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, insira valores numéricos válidos.",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
