package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Contato;
import com.br.pdvfrontend.service.ContatoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ContatoList extends JFrame {

    private final ContatoService contatoService;
    private JTable table;
    private DefaultTableModel tableModel;

    public ContatoList() {
        this.contatoService = new ContatoService();

        setTitle("Cadastro de Contatos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Gerenciamento de Contatos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Endereço"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        btnNovo.setIcon(new FlatSVGIcon("icons/add.svg"));
        JButton btnEditar = new JButton("Editar");
        btnEditar.setIcon(new FlatSVGIcon("icons/edit.svg"));
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setIcon(new FlatSVGIcon("icons/delete.svg"));
        btnExcluir.putClientProperty("JButton.buttonType", "toolBarButton");

        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Contato contatoParaEditar = contatoService.buscarPorId(id);
                abrirFormulario(contatoParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um contato para editar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir este contato?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    contatoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um contato para excluir.");
            }
        });

        atualizarTabela();
    }

    private void abrirFormulario(Contato contato) {
        ContatoForm form = new ContatoForm(this, this, contatoService, contato);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Contato> contatos = contatoService.listar();
        for (Contato c : contatos) {
            if (c == null) continue;
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getTelefone(),
                    c.getEmail(),
                    c.getEndereco()
            });
        }
    }
}
