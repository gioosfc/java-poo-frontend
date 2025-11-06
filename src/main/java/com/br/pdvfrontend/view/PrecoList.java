package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PrecoList extends JFrame {

    private final PrecoService precoService = new PrecoService();
    private JTable tabela;
    private DefaultTableModel modelo;

    public PrecoList() {
        setTitle("Lista de Preços");
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Produto", "Valor", "Data", "Hora"},
                0
        );
        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel();

        JButton novo = new JButton("Novo");
        JButton excluir = new JButton("Excluir");

        panelButtons.add(novo);
        panelButtons.add(excluir);

        add(panelButtons, BorderLayout.SOUTH);

        novo.addActionListener(e -> abrirForm(null));

        excluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Long id = (Long) tabela.getValueAt(linha, 0);
                precoService.deletar(id);
                atualizarTabela();
            }
        });

        atualizarTabela();
    }

    public void atualizarTabela() {
        modelo.setRowCount(0);
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfH = new SimpleDateFormat("HH:mm:ss");

        try {
            List<Preco> precos = precoService.listar();
            for (Preco p : precos) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNomeProduto(),
                        p.getValor(),
                        sdfD.format(p.getDataAlteracao()),
                        sdfH.format(p.getHoraAlteracao())
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
