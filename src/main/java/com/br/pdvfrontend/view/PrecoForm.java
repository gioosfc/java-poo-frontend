package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;

public class PrecoForm extends JDialog {

    private final PrecoList ownerList;
    private final PrecoService precoService;
    private Preco preco; // O objeto sendo editado, ou null se for novo

    private JTextField valorField;

    public PrecoForm(Frame owner, PrecoList ownerList, PrecoService precoService, Preco preco) {
        super(owner, (preco == null) ? "Novo Preço" : "Editar Preço", true);
        this.ownerList = ownerList;
        this.precoService = precoService;
        this.preco = preco;

        setTitle("Cadastro de Preço");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Valor:"));
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
        }
    }

    private void onSalvar() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText());
            Date dataAtual = new Date();

            if (preco == null) {
                preco = new Preco();
            }

            preco.setValor(valor);
            preco.setDataAlteracao(dataAtual);
            preco.setHoraAlteracao(dataAtual);

            precoService.salvar(preco);

            ownerList.atualizarTabela();

            JOptionPane.showMessageDialog(this, "Preço salvo com sucesso!");
            dispose(); // Close the form after saving
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
