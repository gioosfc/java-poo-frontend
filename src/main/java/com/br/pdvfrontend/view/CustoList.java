package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustoList extends JFrame {

    private final CustoService custoService = new CustoService();
    private JTable tabela;
    private DefaultTableModel modelo;

    public CustoList() {
        setTitle("Lista de Custos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Gerenciamento de Custos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Produto", "Imposto", "Variável", "Lucro", "Fixo", "Data"},
                0
        );
        tabela = new JTable(modelo);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        btnNovo.setIcon(new FlatSVGIcon("icons/add.svg"));
        JButton btnEditar = new JButton("Editar");
        btnEditar.setIcon(new FlatSVGIcon("icons/edit.svg"));
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setIcon(new FlatSVGIcon("icons/delete.svg"));
        btnExcluir.putClientProperty("JButton.buttonType", "toolBarButton");

        panelButtons.add(btnNovo);
        panelButtons.add(btnEditar);
        panelButtons.add(btnExcluir);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);

        add(mainPanel);

        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Long id = (Long) tabela.getValueAt(linha, 0);
                Custos c = custoService.buscarPorId(id);
                abrirFormulario(c);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um custo para editar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir este custo?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tabela.getValueAt(linha, 0);
                    custoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um custo para excluir.");
            }
        });

        atualizarTabela();
    }

    public void atualizarTabela() {
        modelo.setRowCount(0);
        try {
            List<Custos> custos = custoService.listar();
            for (Custos c : custos) {
                modelo.addRow(new Object[]{
                        c.getId(),
                        c.getNomeProduto(),
                        c.getImposto(),
                        c.getCustoVariaveis(),
                        c.getMargemLucro(),
                        c.getCustoFixo(),
                        c.getDataProcessamento()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar custos!");
        }
    }

    private void abrirFormulario(Custos custo) {
        CustoForm form = new CustoForm(this, this, custoService, custo);
        form.setVisible(true);
    }
}
