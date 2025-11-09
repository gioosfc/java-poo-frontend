package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.*;
import com.br.pdvfrontend.request.VendaRequest;
import com.br.pdvfrontend.service.ProdutoService;
import com.br.pdvfrontend.service.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Tela principal de vendas do PDV - exibe as 3 bombas,
 * carrega produtos reais do backend e gera comprovante PDF.
 */
public class VendaForm extends JDialog {

    private final VendaService vendaService = new VendaService();
    private final ProdutoService produtoService = new ProdutoService();

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtPlaca;
    private JComboBox<String> cbFormaPgto;

    // referência de produtos (carregados do backend)
    private Produto prod1, prod2, prod3;

    public VendaForm(Frame owner) {
        super(owner, "Nova Venda", true);
        setSize(950, 680);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Topo: placa + forma de pagamento
        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(new JLabel("Placa:"));
        txtPlaca = new JTextField(8);
        north.add(txtPlaca);

        north.add(new JLabel("Forma Pgto:"));
        cbFormaPgto = new JComboBox<>(new String[]{"DINHEIRO", "PIX", "CARTAO"});
        north.add(cbFormaPgto);
        add(north, BorderLayout.NORTH);

        // === Painel de Bombas ===
        JPanel bombas = new JPanel(new GridLayout(1, 3, 10, 10));
        carregarProdutosEBombas(bombas);

        // === Tabela de Itens ===
        model = new DefaultTableModel(new Object[]{"Bomba ID", "Bomba Nome", "Produto", "Litros (L)"}, 0);
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(table);

        // === Split: bombas (topo) + tabela (baixo) ===
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bombas, sp);
        split.setResizeWeight(0.6); // ~60% espaço para as bombas
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        // === Rodapé com ações ===
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLimpar = new JButton("Limpar Itens");
        btnLimpar.addActionListener(e -> model.setRowCount(0));
        JButton btnConcluir = new JButton("Concluir Venda");
        btnConcluir.addActionListener(e -> concluirVenda());
        south.add(btnLimpar);
        south.add(btnConcluir);
        add(south, BorderLayout.SOUTH); // importante: SOUTH, não PAGE_END
    }

    /**
     * Busca produtos do backend e cria as bombas dinamicamente.
     */
    private void carregarProdutosEBombas(JPanel bombas) {
        try {
            List<Produto> produtos = produtoService.listar();

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum produto encontrado no sistema. Cadastre produtos primeiro.");
                return;
            }

            if (produtos.size() < 3) {
                JOptionPane.showMessageDialog(this,
                        "É necessário ter pelo menos 3 produtos cadastrados para as bombas.");
                return;
            }

            prod1 = produtos.get(0);
            prod2 = produtos.get(1);
            prod3 = produtos.get(2);

            bombas.add(cardBomba(1L, "Bomba 1 - " + prod1.getNome(), prod1));
            bombas.add(cardBomba(2L, "Bomba 2 - " + prod2.getNome(), prod2));
            bombas.add(cardBomba(3L, "Bomba 3 - " + prod3.getNome(), prod3));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Cria o painel de uma bomba com produto e preço.
     */
    private JPanel cardBomba(Long bombaId, String bombaNome, Produto produto) {
        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder(bombaNome));

        JLabel lblProd = new JLabel("Produto: " + produto.getNome());
        JLabel lblPreco = new JLabel("Preço/L: R$ " +
                (produto.getPrecoVenda() != null ? produto.getPrecoVenda() : "0.00"));

        JLabel lblLitros = new JLabel("Litros:");

        JTextField txtLitros = new JTextField();
        JButton btnAdd = new JButton("Adicionar");

        btnAdd.addActionListener(e -> {
            String litrosStr = txtLitros.getText().trim();
            if (litrosStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe os litros abastecidos.");
                return;
            }

            try {
                BigDecimal litros = new BigDecimal(litrosStr.replace(",", "."));
                if (litros.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Litros deve ser > 0.");
                    return;
                }

                model.addRow(new Object[]{
                        bombaId,
                        bombaNome,
                        produto.getNome() + " (ID=" + produto.getId() + ")",
                        litros.toPlainString()
                });

                txtLitros.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido de litros.");
            }
        });

        p.add(lblProd);
        p.add(lblPreco);
        p.add(lblLitros);
        p.add(txtLitros);
        p.add(btnAdd);

        return p;
    }

    /**
     * Monta o objeto Venda e envia ao backend.
     */
    private void concluirVenda() {
        try {
            String usuario = SessaoUsuario.getUsuario();
            String papel = SessaoUsuario.getPapel();

            if (usuario == null || usuario.isBlank()) {
                JOptionPane.showMessageDialog(this, "Nenhum usuário logado.");
                return;
            }

            List<VendaItem> itens = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                Long bombaId = (Long) model.getValueAt(i, 0);
                String bombaNome = (String) model.getValueAt(i, 1);
                String prodTxt = (String) model.getValueAt(i, 2);
                String litrosStr = (String) model.getValueAt(i, 3);

                // extrai o ID real do produto da string
                Long produtoId = Long.parseLong(
                        prodTxt.substring(prodTxt.indexOf("ID=") + 3, prodTxt.indexOf(")"))
                );

                BigDecimal litros = new BigDecimal(litrosStr.replace(",", "."));

                Produto produto = new Produto();
                produto.setId(produtoId);

                VendaItem vi = new VendaItem();
                vi.setBombaId(bombaId);
                vi.setBombaNome(bombaNome);
                vi.setProduto(produto);
                vi.setQuantidade(litros);

                itens.add(vi);
            }

            if (itens.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum item na venda.");
                return;
            }
            var a = cbFormaPgto.getSelectedItem();
            String forma = (String) cbFormaPgto.getSelectedItem();
            String placa = txtPlaca.getText().trim();

            Venda venda = new Venda();
            venda.setUsuario(usuario);
            venda.setFormaPagamento(forma);
            venda.setPlaca(placa.isEmpty() ? null : placa);
            venda.setItens(itens);

            Venda resposta = vendaService.criarVenda(venda, placa, forma);

            JOptionPane.showMessageDialog(this,
                    "Venda #" + resposta.getId() + " criada com sucesso!\nTotal: R$ " + resposta.getTotal(),
                    "Venda Concluída", JOptionPane.INFORMATION_MESSAGE);

            // baixar PDF
            byte[] pdf = vendaService.baixarComprovante(resposta.getId());
            Path tmp = Files.createTempFile("comprovante-" + resposta.getId(), ".pdf");
            Files.write(tmp, pdf);
            Desktop.getDesktop().open(tmp.toFile());

            model.setRowCount(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao concluir venda: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
