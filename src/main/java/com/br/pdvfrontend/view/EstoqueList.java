package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Estoque;
import com.br.pdvfrontend.service.EstoqueService;
import com.br.pdvfrontend.service.ProdutoService;

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

        setTitle("Estoque");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        model = new DefaultTableModel(new String[]{"ID","Produto","Referência","Quantidade"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        buttons.add(btnNovo); buttons.add(btnEditar); buttons.add(btnExcluir);

        btnNovo.addActionListener(e -> abrirForm(null));

        btnEditar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um estoque.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(row, 0);
            Estoque est = estoqueService.buscarPorId(id);
            abrirForm(est);
        });

        btnExcluir.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um estoque.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Excluir o registro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                estoqueService.deletar(id);
                atualizarTabela();
            }
        });

        main.add(buttons, BorderLayout.SOUTH);
        setContentPane(main);

        atualizarTabela();
    }

    private void abrirForm(Estoque estoque) {
        EstoqueForm form = new EstoqueForm(this, this, estoqueService, produtoService, estoque);
        form.setVisible(true);
    }

    public void atualizarTabela() {
        List<Estoque> lista = estoqueService.listar();
        model.setRowCount(0);
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
