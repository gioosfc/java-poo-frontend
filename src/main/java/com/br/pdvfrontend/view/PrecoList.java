package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrecoList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton novoButton;
    private JButton atualizarButton;

    public PrecoList() {
        setTitle("Lista de Preços");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        String[] columnNames = {"Valor", "Data Alteração", "Hora Alteração"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel();
        novoButton = new JButton("Novo Preço");
        atualizarButton = new JButton("Atualizar");
        buttonPanel.add(novoButton);
        buttonPanel.add(atualizarButton);

        // Layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Actions
        novoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrecoForm precoForm = new PrecoForm();
                precoForm.setVisible(true);
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarTabela();
            }
        });

        // Initial data load
        atualizarTabela();
    }

    private void atualizarTabela() {
        // Clear existing data
        tableModel.setRowCount(0);

        // Get data from service
        List<Preco> precos = PrecoService.getInstance().getPrecos();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        // Populate table
        for (Preco preco : precos) {
            Object[] rowData = {
                    preco.getValor(),
                    sdfDate.format(preco.getDataAlteracao()),
                    sdfTime.format(preco.getHoraAlteracao())
            };
            tableModel.addRow(rowData);
        }
    }
}
