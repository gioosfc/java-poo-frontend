package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrecoList extends JFrame {

    private final PrecoService precoService;
    private JTable table;
    private DefaultTableModel tableModel;

    public PrecoList() {
        this.precoService = new PrecoService();

        setTitle("Lista de Preços");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Valor", "Data Alteração", "Hora Alteração"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);

        // Layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Ações
        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Preco precoParaEditar = precoService.buscarPorId(id);
                abrirFormulario(precoParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um preço para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Excluir Preço", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    precoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um preço para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Carrega os dados iniciais
        atualizarTabela();
    }

    private void abrirFormulario(Preco preco) {
        PrecoForm form = new PrecoForm(this, this, precoService, preco);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Preco> precos = precoService.listar();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        for (Preco preco : precos) {
            Object[] rowData = {
                    preco.getId(),
                    preco.getValor(),
                    sdfDate.format(preco.getDataAlteracao()),
                    sdfTime.format(preco.getHoraAlteracao())
            };
            tableModel.addRow(rowData);
        }
    }
}
