package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

public class PrecoForm extends JFrame {

    private JTextField valorField;
    private JButton salvarButton;

    public PrecoForm() {
        setTitle("Cadastro de Preço");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Valor:"));
        valorField = new JTextField();
        panel.add(valorField);

        salvarButton = new JButton("Salvar");
        panel.add(new JLabel()); // empty label for spacing
        panel.add(salvarButton);

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPreco();
            }
        });

        add(panel);
    }

    private void salvarPreco() {
        try {
            BigDecimal valor = new BigDecimal(valorField.getText());
            Date dataAtual = new Date();

            Preco novoPreco = new Preco(valor, dataAtual, dataAtual);
            PrecoService.getInstance().addPreco(novoPreco);

            JOptionPane.showMessageDialog(this, "Preço salvo com sucesso!");
            dispose(); // Close the form after saving
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um valor numérico válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}
