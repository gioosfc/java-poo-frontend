package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Custos;
import com.br.pdvfrontend.service.CustoService;

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
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Produto", "Imposto", "Variável", "Lucro", "Fixo", "Data"},
                0
        );
        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();

        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");

        panelButtons.add(btnNovo);
        panelButtons.add(btnEditar);
        panelButtons.add(btnExcluir);

        add(panelButtons, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> abrirFormulario(null));

        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Custos c = getCustoFromRow(linha);
                abrirFormulario(c);
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Long id = (Long) tabela.getValueAt(linha, 0);

                try {
                    custoService.deletar(id);
                    atualizarTabela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir custo!");
                }
            }
        });

        atualizarTabela();
    }

    private Custos getCustoFromRow(int linha) {
        Custos c = new Custos();
        c.setId((Long) tabela.getValueAt(linha, 0));
        c.setNomeProduto((String) tabela.getValueAt(linha, 1)); // novo

        c.setImposto((Double) tabela.getValueAt(linha, 2));
        c.setCustoVariaveis((Double) tabela.getValueAt(linha, 3));
        c.setMargemLucro((Double) tabela.getValueAt(linha, 4));
        c.setCustoFixo((Double) tabela.getValueAt(linha, 5));
        c.setDataProcessamento((java.util.Date) tabela.getValueAt(linha, 6));

        return c;
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
