package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.service.EstoqueService;
import com.br.pdvfrontend.service.ProdutoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstoqueList extends JFrame {

    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;

    private JTable table;
    private DefaultTableModel model;

    public EstoqueList() {
        this.estoqueService = new EstoqueService();
        this.produtoService = new ProdutoService();

        setTitle("Controle de Estoque");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Gerenciamento de Estoque", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 20));
        main.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Produto", "Referência", "Quantidade"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        btnNovo.setIcon(new FlatSVGIcon("icons/add.svg"));
        JButton btnEditar = new JButton("Editar");
        btnEditar.setIcon(new FlatSVGIcon("icons/edit.svg"));
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setIcon(new FlatSVGIcon("icons/delete.svg"));
        btnExcluir.putClientProperty("JButton.buttonType", "toolBarButton");

        buttons.add(btnNovo);
        buttons.add(btnEditar);
        buttons.add(btnExcluir);
        main.add(buttons, BorderLayout.SOUTH);

        setContentPane(main);

        btnNovo.addActionListener(e -> abrirForm(null));

        btnEditar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um item do estoque para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(row, 0);
            Estoque est = estoqueService.buscarPorId(id);
            abrirForm(est);
        });

        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um item do estoque para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este item do estoque?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                Long id = (Long) model.getValueAt(row, 0);
                estoqueService.deletar(id);
                atualizarTabela();
            }
        });

        atualizarTabela();
    }

    private void abrirForm(Estoque estoque) {
        EstoqueForm form = new EstoqueForm(this, this, estoqueService, produtoService, estoque);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        model.setRowCount(0);
        List<Estoque> lista = estoqueService.listar();
        for (Estoque e : lista) {
            if (e == null) continue;
            model.addRow(new Object[]{
                    e.getId(),
                    e.getProdutoNome(),
                    e.getProdutoReferencia(),
                    e.getQuantidade()
            });
        }
    }
}
