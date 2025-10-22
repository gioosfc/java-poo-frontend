package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AcessoList extends JFrame {

    private final List<Acesso> listaDeAcessos = new ArrayList<>();
    private JTable table;
    private DefaultTableModel tableModel;

    public AcessoList() {
        // Configurações da janela
        setTitle("Cadastro de Acessos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Adiciona dados de exemplo
        listaDeAcessos.add(new Acesso("admin", "admin123"));
        listaDeAcessos.add(new Acesso("caixa", "caixa123"));

        // --- Componentes da Tela ---
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela
        String[] colunas = {"Usuário"};
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

        // Ação para o botão "Novo"
        btnNovo.addActionListener(e -> {
            // Cria e exibe o formulário para um novo acesso
            AcessoForm form = new AcessoForm(this, this, null);
            form.setVisible(true);
        });

        // Ação para o botão "Editar"
        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Pega o objeto da lista e o passa para o formulário
                Acesso acessoParaEditar = listaDeAcessos.get(selectedRow);
                AcessoForm form = new AcessoForm(this, this, acessoParaEditar);
                form.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um acesso para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Ação para o botão "Excluir"
        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza?", "Excluir Acesso", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    listaDeAcessos.remove(selectedRow);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um acesso para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Carrega os dados iniciais na tabela
        atualizarTabela();
    }

    /**
     * Atualiza a tabela com os dados da lista em memória.
     */
    public void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Acesso acesso : listaDeAcessos) {
            tableModel.addRow(new Object[]{acesso.getUsuario()});
        }
    }

    /**
     * Método para ser chamado pelo formulário para salvar um acesso.
     */
    public void salvarAcesso(Acesso acesso) {
        if (!listaDeAcessos.contains(acesso)) {
            listaDeAcessos.add(acesso);
        }
        atualizarTabela();
    }
}
