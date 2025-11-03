package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AcessoList extends JFrame {

    private final AcessoService acessoService;
    private JTable table;
    private DefaultTableModel tableModel;

    public AcessoList() {
        this.acessoService = new AcessoService();

        setTitle("Cadastro de Acessos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"ID", "Usuário", "Senha"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
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
                Acesso acessoParaEditar = acessoService.buscarPorId(id);
                abrirFormulario(acessoParaEditar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um acesso para editar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                acessoService.deletar(id);
                atualizarTabela();
            }
        });

        atualizarTabela();
    }

    private void abrirFormulario(Acesso acesso) {
        AcessoForm form = new AcessoForm(this, acessoService, acesso, this);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        List<Acesso> acessos = acessoService.listar();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Usuário", "Senha"}, 0);
        for (Acesso acesso : acessos) {
            model.addRow(new Object[]{acesso.getId(), acesso.getUsuario(), acesso.getSenha()});
        }
        table.setModel(model);
    }
}
