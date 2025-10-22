package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class CustoForm extends JFrame {

    private JTextField impostoField;
    private JTextField custoVariaveisField;
    private JTextField margemLucroField;
    private JTextField custoFixoField;
    private JButton salvarButton;

    public CustoForm() {
        setTitle("Cadastro de Custo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

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

        salvarButton = new JButton("Salvar");
        panel.add(new JLabel()); // empty label for spacing
        panel.add(salvarButton);

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarCusto();
            }
        });

        add(panel);
    }

    private void salvarCusto() {
        try {
            double imposto = Double.parseDouble(impostoField.getText());
            double custoVariaveis = Double.parseDouble(custoVariaveisField.getText());
            double margemLucro = Double.parseDouble(margemLucroField.getText());
            double custoFixo = Double.parseDouble(custoFixoField.getText());
            Date dataProcessamento = new Date(); // Using current date for simplicity

            Custos novoCusto = new Custos(imposto, custoFixo, custoVariaveis, margemLucro, dataProcessamento);
            CustoService.getInstance().addCusto(novoCusto);

            JOptionPane.showMessageDialog(this, "Custo salvo com sucesso!");
            dispose(); // Close the form after saving
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira valores numéricos válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
