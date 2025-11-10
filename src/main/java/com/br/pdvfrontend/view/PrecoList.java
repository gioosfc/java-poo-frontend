package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrecoList extends JFrame {

    private final PrecoService precoService = new PrecoService();
    private JTable tabela;
    private DefaultTableModel modelo;

    public PrecoList() {
        setTitle("Lista de Preços");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Gerenciamento de Preços", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Produto", "Valor", "Data Alteração"},
                0
        );
        tabela = new JTable(modelo);
        mainPanel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton novo = new JButton("Novo");
        novo.setIcon(new FlatSVGIcon("icons/add.svg"));
        JButton excluir = new JButton("Excluir");
        excluir.setIcon(new FlatSVGIcon("icons/delete.svg"));
        excluir.putClientProperty("JButton.buttonType", "toolBarButton");

        panelButtons.add(novo);
        panelButtons.add(excluir);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);

        add(mainPanel);

        novo.addActionListener(e -> abrirForm(null));

        excluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Tem certeza que deseja excluir este preço?",
                        "Confirmar Exclusão",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    Long id = (Long) tabela.getValueAt(linha, 0);
                    precoService.deletar(id);
                    atualizarTabela();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um preço para excluir.");
            }
        });

        atualizarTabela();
    }

    public void atualizarTabela() {
        try {
            modelo.setRowCount(0);
            List<Preco> precos = precoService.listar();
            if (precos == null || precos.isEmpty()) {
                return;
            }

            for (Preco preco : precos) {
                String data = preco.getDataAlteracao() != null ? preco.getDataAlteracao().replace("T", " ") : "-";
                modelo.addRow(new Object[]{
                        preco.getId(),
                        preco.getNomeProduto() != null ? preco.getNomeProduto() : "(Sem nome)",
                        preco.getValor(),
                        data
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar preços.");
            e.printStackTrace();
        }
    }

    private void abrirForm(Preco preco) {
        PrecoForm form = new PrecoForm(this, this, precoService, preco);
        form.setVisible(true);
    }
}
