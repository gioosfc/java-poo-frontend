package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustoList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton novoButton;
    private JButton atualizarButton;

    public CustoList() {
        setTitle("Lista de Custos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        String[] columnNames = {"Imposto", "Custo Fixo", "Custo Variável", "Margem de Lucro", "Data de Processamento"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel();
        novoButton = new JButton("Novo Custo");
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
                CustoForm custoForm = new CustoForm();
                custoForm.setVisible(true);
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
        List<Custos> custos = CustoService.getInstance().getCustos();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Populate table
        for (Custos custo : custos) {
            Object[] rowData = {
                    custo.getImposto(),
                    custo.getCustoFixo(),
                    custo.getCustoVariaveis(),
                    custo.getMargemLucro(),
                    sdf.format(custo.getDataProcessamento())
            };
            tableModel.addRow(rowData);
        }
    }
}
