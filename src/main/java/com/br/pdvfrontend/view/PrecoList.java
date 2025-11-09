package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Preco;
import com.br.pdvfrontend.service.PrecoService;

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
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Produto", "Valor", "Data Alteração"},
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

    /**
     * Atualiza a tabela de preços, buscando do backend
     */
    public void atualizarTabela() {
        try {
            modelo.setRowCount(0); // limpa tabela

            List<Preco> precos = precoService.listar();
            if (precos == null || precos.isEmpty()) {
                return;
            }

            for (Preco preco : precos) {
                String data = "-";

                try {
                    if (preco.getDataAlteracao() != null) {
                        // formata LocalDateTime retornado como string ISO
                        data = preco.getDataAlteracao().replace("T", " ");
                    }
                } catch (Exception e) {
                    data = "-";
                }

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
