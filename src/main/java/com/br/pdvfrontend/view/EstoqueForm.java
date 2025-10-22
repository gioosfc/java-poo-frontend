package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.service.EstoqueService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EstoqueForm extends JFrame {

    private JTextField quantidadeField;
    private JTextField localTanqueField;
    private JTextField localEnderecoField;
    private JTextField loteFabricacaoField;
    private JTextField dataDeValidadeField;
    private JButton salvarButton;

    public EstoqueForm() {
        setTitle("Cadastro de Estoque");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        panel.add(quantidadeField);

        panel.add(new JLabel("Local Tanque:"));
        localTanqueField = new JTextField();
        panel.add(localTanqueField);

        panel.add(new JLabel("Local Endereço:"));
        localEnderecoField = new JTextField();
        panel.add(localEnderecoField);

        panel.add(new JLabel("Lote de Fabricação:"));
        loteFabricacaoField = new JTextField();
        panel.add(loteFabricacaoField);

        panel.add(new JLabel("Data de Validade (dd/MM/yyyy):"));
        dataDeValidadeField = new JTextField();
        panel.add(dataDeValidadeField);

        salvarButton = new JButton("Salvar");
        panel.add(new JLabel()); // empty label for spacing
        panel.add(salvarButton);

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarEstoque();
            }
        });

        add(panel);
    }

    private void salvarEstoque() {
        try {
            BigDecimal quantidade = new BigDecimal(quantidadeField.getText());
            String localTanque = localTanqueField.getText();
            String localEndereco = localEnderecoField.getText();
            String loteFabricacao = loteFabricacaoField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataDeValidade = sdf.parse(dataDeValidadeField.getText());

            if (localTanque.isEmpty() || localEndereco.isEmpty() || loteFabricacao.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Estoque novoEstoque = new Estoque(quantidade, localEndereco, localTanque, loteFabricacao, dataDeValidade);
            EstoqueService.getInstance().addEstoque(novoEstoque);

            JOptionPane.showMessageDialog(this, "Estoque salvo com sucesso!");
            dispose(); // Close the form after saving
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira uma quantidade numérica válida.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira a data no formato dd/MM/yyyy.", "Erro de Formato da Data", JOptionPane.ERROR_MESSAGE);
        }
    }
}
