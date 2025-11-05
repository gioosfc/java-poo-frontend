package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Contato;
import com.br.pdvfrontend.service.ContatoService;

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

        // Configurações da janela
        setTitle("Cadastro de Contatos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Componentes da Tela ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] colunas = {"ID", "Nome", "Telefone", "Email", "Endereço"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // --- Ações dos Botões ---
        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Contato contatoParaEditar = contatoService.buscarPorId(id);
                abrirFormulario(contatoParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um contato para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Excluir Contato", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                    //contatoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um contato para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Carrega os dados iniciais
        atualizarTabela();
    }

    private void abrirFormulario(Contato contato) {
        ContatoForm form = new ContatoForm(this, this, contatoService, contato);
        form.setVisible(true);
    }

    public void atualizarTabela() {

        List<Contato> contatos = contatoService.listar();
        tableModel.setRowCount(0);

        for (Contato c : contatos) {

            if (c == null) continue; // segurança absoluta

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
