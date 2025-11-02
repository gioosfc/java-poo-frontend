package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class CustoForm extends JDialog {

    private final CustoList ownerList;
    private final CustoService custoService;
    private Custos custo; // O objeto sendo editado, ou null se for novo

    private JTextField impostoField;
    private JTextField custoVariaveisField;
    private JTextField margemLucroField;
    private JTextField custoFixoField;

    public CustoForm(Frame owner, CustoList ownerList, CustoService custoService, Custos custo) {
        super(owner, (custo == null) ? "Novo Custo" : "Editar Custo", true);
        this.ownerList = ownerList;
        this.custoService = custoService;
        this.custo = custo;

        setTitle("Cadastro de Custo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        if (custo != null) {
            impostoField.setText(String.valueOf(custo.getImposto()));
            custoVariaveisField.setText(String.valueOf(custo.getCustoVariaveis()));
            margemLucroField.setText(String.valueOf(custo.getMargemLucro()));
            custoFixoField.setText(String.valueOf(custo.getCustoFixo()));
        }
    }

    private void onSalvar() {
        try {
            double imposto = Double.parseDouble(impostoField.getText());
            double custoVariaveis = Double.parseDouble(custoVariaveisField.getText());
            double margemLucro = Double.parseDouble(margemLucroField.getText());
            double custoFixo = Double.parseDouble(custoFixoField.getText());
            Date dataProcessamento = new Date(); // Using current date for simplicity

            if (custo == null) {
                custo = new Custos();
            }

            custo.setImposto(imposto);
            custo.setCustoVariaveis(custoVariaveis);
            custo.setMargemLucro(margemLucro);
            custo.setCustoFixo(custoFixo);
            custo.setDataProcessamento(dataProcessamento);

            custoService.salvar(custo);

            ownerList.atualizarTabela();

            JOptionPane.showMessageDialog(this, "Custo salvo com sucesso!");
            dispose(); // Close the form after saving
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
