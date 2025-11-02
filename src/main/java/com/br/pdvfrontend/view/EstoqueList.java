package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.service.EstoqueService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class EstoqueList extends JFrame {

    private final EstoqueService estoqueService;
    private JTable table;
    private DefaultTableModel tableModel;

    public EstoqueList() {
        this.estoqueService = new EstoqueService();

        setTitle("Lista de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela
        String[] columnNames = {"ID", "Quantidade", "Local Tanque", "Local Endereço", "Lote Fabricação", "Data de Validade"};
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
                Estoque estoqueParaEditar = estoqueService.buscarPorId(id);
                abrirFormulario(estoqueParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um estoque para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Excluir Estoque", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    estoqueService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um estoque para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Carrega os dados iniciais
        atualizarTabela();
    }

    private void abrirFormulario(Estoque estoque) {
        EstoqueForm form = new EstoqueForm(this, this, estoqueService, estoque);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Estoque> estoques = estoqueService.listar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Estoque estoque : estoques) {
            Object[] rowData = {
                    estoque.getId(),
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
