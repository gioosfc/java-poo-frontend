package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.service.PrecoService;
import com.br.pdvfrontend.service.ProdutoService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;

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

        setTitle("Cadastro de Preço");
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Produto:"));
        produtoCombo = new JComboBox<>();
        for (Produto p : produtoService.listar()) produtoCombo.addItem(p);
        panel.add(produtoCombo);

        panel.add(new JLabel("Valor Venda:"));
        valorField = new JTextField();
        panel.add(valorField);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        panel.add(btnSalvar);
        panel.add(btnCancelar);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);

        if (preco != null) {
            valorField.setText(preco.getValor().toString());
            Produto atual = produtoService.buscarPorId(preco.getProdutoId());
            produtoCombo.setSelectedItem(atual);
        }
    }

    private void onSalvar() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText());
            Date dataAtual = new Date();

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
            preco.setDataAlteracao(dataAtual);
            preco.setHoraAlteracao(dataAtual);

            precoService.salvar(preco);

            ownerList.atualizarTabela();

            JOptionPane.showMessageDialog(this, "Preço salvo com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar!");
            ex.printStackTrace();
        }
    }
}
