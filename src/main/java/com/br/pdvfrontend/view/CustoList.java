package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CustoList extends JFrame {

    private final CustoService custoService;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustoList() {
        this.custoService = new CustoService();

        setTitle("Lista de Custos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Imposto", "Custo Fixo", "Custo Variável", "Margem de Lucro", "Data de Processamento"};
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
                Custos custoParaEditar = custoService.buscarPorId(id);
                abrirFormulario(custoParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um custo para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Excluir Custo", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    custoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um custo para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Carrega os dados iniciais
        atualizarTabela();
    }

    private void abrirFormulario(Custos custo) {
        CustoForm form = new CustoForm(this, this, custoService, custo);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Custos> custos = custoService.listar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Custos custo : custos) {
            Object[] rowData = {
                    custo.getId(),
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
