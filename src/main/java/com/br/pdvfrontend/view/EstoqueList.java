package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.service.EstoqueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class EstoqueList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton novoButton;
    private JButton atualizarButton;

    public EstoqueList() {
        setTitle("Lista de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        String[] columnNames = {"Quantidade", "Local Tanque", "Local Endereço", "Lote Fabricação", "Data de Validade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons
        JPanel buttonPanel = new JPanel();
        novoButton = new JButton("Novo Estoque");
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
                EstoqueForm estoqueForm = new EstoqueForm();
                estoqueForm.setVisible(true);
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
        List<Estoque> estoques = EstoqueService.getInstance().getEstoques();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Populate table
        for (Estoque estoque : estoques) {
            Object[] rowData = {
                    estoque.getQuantidade(),
                    estoque.getLocalTanque(),
                    estoque.getLocalEndereco(),
                    estoque.getLoteFabricacao(),
                    sdf.format(estoque.getDataDeValidade())
            };
            tableModel.addRow(rowData);
        }
    }
}
