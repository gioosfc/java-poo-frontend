package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Produto;
import com.br.pdvfrontend.model.Venda;
import com.br.pdvfrontend.model.VendaItem;
import com.br.pdvfrontend.service.VendaService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

/**
 * Relatório de Vendas com filtros por período, forma de pagamento e placa.
 * - Tabela de vendas
 * - Aba de itens da venda selecionada
 * - Aba de resumo por produto no período (via endpoint resumo)
 * - Exportar CSV
 * - Abrir comprovante (PDF) da venda selecionada
 */
public class RelatorioVendasForm extends JDialog {

    private final VendaService vendaService = new VendaService();

    // Filtros
    private final JSpinner spInicio;
    private final JSpinner spFim;
    private final JComboBox<String> cbForma;
    private final JTextField txtPlaca;

    // Tabelas
    private final JTable tblVendas;
    private final JTable tblItens;
    private final JTable tblResumoProduto;

    private final DefaultTableModel modelVendas;
    private final DefaultTableModel modelItens;
    private final DefaultTableModel modelResumoProduto;

    // Dados carregados
    private List<Venda> vendas = new ArrayList<>();

    // Rodapé (resumo)
    private final JLabel lbQtdVendas = new JLabel("0");
    private final JLabel lbTotalLitros = new JLabel("0,000 L");
    private final JLabel lbTotalValor = new JLabel("R$ 0,00");

    private final NumberFormat nfMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public RelatorioVendasForm(Frame owner) {
        super(owner, "Relatório de Vendas", true);
        setSize(1100, 700);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // ======= TOPO: FILTROS =======
        JPanel filtros = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = 0; c.anchor = GridBagConstraints.WEST;

        filtros.add(new JLabel("Início:"), gbc(c, 0));
        spInicio = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor edIni = new JSpinner.DateEditor(spInicio, "dd/MM/yyyy");
        spInicio.setEditor(edIni);
        filtros.add(spInicio, gbc(c, 1));

        filtros.add(new JLabel("Fim:"), gbc(c, 2));
        spFim = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor edFim = new JSpinner.DateEditor(spFim, "dd/MM/yyyy");
        spFim.setEditor(edFim);
        filtros.add(spFim, gbc(c, 3));

        filtros.add(new JLabel("Forma:"), gbc(c, 4));
        cbForma = new JComboBox<>(new String[]{"TODAS", "DINHEIRO", "PIX", "CARTAO"});
        filtros.add(cbForma, gbc(c, 5));

        filtros.add(new JLabel("Placa:"), gbc(c, 6));
        txtPlaca = new JTextField(8);
        filtros.add(txtPlaca, gbc(c, 7));

        JButton btHoje = new JButton("Hoje");
        btHoje.addActionListener(this::presetHoje);
        filtros.add(btHoje, gbc(c, 8));

        JButton bt7 = new JButton("Últimos 7 dias");
        bt7.addActionListener(this::preset7Dias);
        filtros.add(bt7, gbc(c, 9));

        JButton btMes = new JButton("Este mês");
        btMes.addActionListener(this::presetMes);
        filtros.add(btMes, gbc(c, 10));

        JButton btBuscar = new JButton("Buscar");
        btBuscar.addActionListener(e -> carregarDados());
        filtros.add(btBuscar, gbc(c, 11));

        JButton btLimpar = new JButton("Limpar");
        btLimpar.addActionListener(e -> { cbForma.setSelectedIndex(0); txtPlaca.setText(""); presetHoje(null); });
        filtros.add(btLimpar, gbc(c, 12));

        add(filtros, BorderLayout.NORTH);

        // ======= CENTRO: TABELAS =======
        modelVendas = new DefaultTableModel(new Object[]{
                "ID", "Data/Hora", "Usuário", "Forma", "Placa", "Qtd Itens", "Total (R$)"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblVendas = new JTable(modelVendas);
        tblVendas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblVendas.getSelectionModel().addListSelectionListener(this::onVendaSelecionada);

        modelItens = new DefaultTableModel(new Object[]{
                "Bomba", "Produto", "Litros (L)", "Preço Unit.", "Subtotal"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblItens = new JTable(modelItens);

        modelResumoProduto = new DefaultTableModel(new Object[]{
                "Produto", "Qtd (L)", "Total (R$)"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblResumoProduto = new JTable(modelResumoProduto);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Itens da venda selecionada", new JScrollPane(tblItens));
        abas.addTab("Resumo por Produto (período)", new JScrollPane(tblResumoProduto));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblVendas), abas);
        split.setResizeWeight(0.6);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        // ======= RODAPÉ =======
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btCSV = new JButton("Exportar CSV");
        btCSV.addActionListener(e -> exportarCSV());
        JButton btComprovante = new JButton("Abrir comprovante (PDF)");
        btComprovante.addActionListener(e -> abrirComprovanteSelecionado());

        rodape.add(new JLabel("Vendas:"));
        rodape.add(lbQtdVendas);
        rodape.add(Box.createHorizontalStrut(16));
        rodape.add(new JLabel("Total Litros:"));
        rodape.add(lbTotalLitros);
        rodape.add(Box.createHorizontalStrut(16));
        rodape.add(new JLabel("Total Valor:"));
        rodape.add(lbTotalValor);
        rodape.add(Box.createHorizontalStrut(24));
        rodape.add(btCSV);
        rodape.add(btComprovante);

        add(rodape, BorderLayout.SOUTH);

        // Presets iniciais e carga
        presetHoje(null);
        carregarDados();
    }

    private GridBagConstraints gbc(GridBagConstraints base, int gridx) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = gridx;
        return c;
    }

    // ======== PRESETS DE PERÍODO ========
    private void presetHoje(ActionEvent e) {
        Calendar cal = Calendar.getInstance();
        Date fim = truncHora(cal.getTime(), 23, 59, 59, 999);
        Date ini = truncHora(cal.getTime(), 0, 0, 0, 0);
        spInicio.setValue(ini);
        spFim.setValue(fim);
    }

    private void preset7Dias(ActionEvent e) {
        Calendar cal = Calendar.getInstance();
        Date fim = truncHora(cal.getTime(), 23, 59, 59, 999);
        cal.add(Calendar.DAY_OF_MONTH, -6);
        Date ini = truncHora(cal.getTime(), 0, 0, 0, 0);
        spInicio.setValue(ini);
        spFim.setValue(fim);
    }

    private void presetMes(ActionEvent e) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date ini = truncHora(cal.getTime(), 0, 0, 0, 0);
        cal = Calendar.getInstance();
        Date fim = truncHora(cal.getTime(), 23, 59, 59, 999);
        spInicio.setValue(ini);
        spFim.setValue(fim);
    }

    private Date truncHora(Date base, int h, int m, int s, int ms) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.SECOND, s);
        c.set(Calendar.MILLISECOND, ms);
        return c.getTime();
    }

    // ======== CARREGAMENTO ========
    private void carregarDados() {
        // Converte filtros
        Date inicio = (Date) spInicio.getValue();
        Date fim = (Date) spFim.getValue();
        String forma = (String) cbForma.getSelectedItem();
        String placa = txtPlaca.getText().trim();

        if ("TODAS".equalsIgnoreCase(forma)) forma = null;
        if (placa.isEmpty()) placa = null;

        LocalDate ldInicio = inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate ldFim = fim.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        try {
            // 1) Busca vendas via service (sem reflexão)
            vendas = vendaService.listarVendas(ldInicio, ldFim, forma, placa);

            preencherTabelaVendas();
            atualizarRodape();

            if (!vendas.isEmpty()) {
                tblVendas.setRowSelectionInterval(0, 0);
            } else {
                modelItens.setRowCount(0);
            }

            // 2) Busca resumo por produto via endpoint dedicado
            preencherResumoProdutoFromDTO(ldInicio, ldFim, forma, placa);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Não foi possível carregar o relatório: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabelaVendas() {
        modelVendas.setRowCount(0);
        for (Venda v : vendas) {
            String dataStr = extrairDataHora(v);
            String usuario = safe(() -> v.getUsuario());
            String forma = safe(() -> v.getFormaPagamento());
            String placa = safe(() -> v.getPlaca());
            BigDecimal total = safe(() -> v.getTotal(), BigDecimal.ZERO);

            List<VendaItem> itens = safe(() -> v.getItens(), Collections.emptyList());
            int qtdItens = itens != null ? itens.size() : 0;

            modelVendas.addRow(new Object[]{
                    safe(() -> v.getId()),
                    dataStr,
                    usuario,
                    forma,
                    placa,
                    qtdItens,
                    nfMoeda.format(total)
            });
        }
    }

    private void onVendaSelecionada(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        int row = tblVendas.getSelectedRow();
        if (row < 0 || row >= vendas.size()) {
            modelItens.setRowCount(0);
            return;
        }
        Venda v = vendas.get(row);
        preencherItens(v);
    }

    private void preencherItens(Venda v) {
        modelItens.setRowCount(0);
        List<VendaItem> itens = safe(v::getItens, Collections.emptyList());
        for (VendaItem it : itens) {
            String bomba = safe(it::getBombaNome);
            BigDecimal litros = safe(it::getQuantidade, BigDecimal.ZERO);

            // tenta obter preço unitário e subtotal, se existir no model
            BigDecimal unit = extrairPrecoUnitario(it);
            BigDecimal sub = extrairSubtotal(it);
            if (sub == null && unit != null && litros != null) {
                sub = unit.multiply(litros);
            }

            Produto prod = safe(it::getProduto);
            String prodNome = prod != null ? safe(prod::getNome) : null;
            if (prodNome == null) prodNome = "(produto)";

            modelItens.addRow(new Object[]{
                    bomba,
                    prodNome,
                    litros != null ? litros.toPlainString() : "0",
                    unit != null ? nfMoeda.format(unit) : "-",
                    sub != null ? nfMoeda.format(sub) : "-"
            });
        }
    }

    /** Novo preenchimento do resumo usando o DTO vindo do serviço do frontend */
    private void preencherResumoProdutoFromDTO(LocalDate ldInicio, LocalDate ldFim, String forma, String placa) {
        modelResumoProduto.setRowCount(0);
        List<VendaService.ResumoProduto> resumo = vendaService.resumoPorProduto(ldInicio, ldFim, forma, placa);
        // Ordena por total desc
        resumo.sort((a, b) -> {
            BigDecimal ta = a.getTotal() == null ? BigDecimal.ZERO : a.getTotal();
            BigDecimal tb = b.getTotal() == null ? BigDecimal.ZERO : b.getTotal();
            return tb.compareTo(ta);
        });
        for (VendaService.ResumoProduto r : resumo) {
            modelResumoProduto.addRow(new Object[]{
                    r.getProduto(),
                    r.getLitros() != null ? r.getLitros().toPlainString() : "0",
                    r.getTotal() != null ? nfMoeda.format(r.getTotal()) : "-"
            });
        }
    }

    private void atualizarRodape() {
        lbQtdVendas.setText(String.valueOf(vendas.size()));

        BigDecimal totalValor = BigDecimal.ZERO;
        BigDecimal totalLitros = BigDecimal.ZERO;

        for (Venda v : vendas) {
            totalValor = totalValor.add(safe(v::getTotal, BigDecimal.ZERO));
            List<VendaItem> itens = safe(v::getItens, Collections.emptyList());
            for (VendaItem it : itens) {
                totalLitros = totalLitros.add(safe(it::getQuantidade, BigDecimal.ZERO));
            }
        }

        lbTotalValor.setText(nfMoeda.format(totalValor));
        lbTotalLitros.setText(totalLitros.toPlainString() + " L");
    }

    private void exportarCSV() {
        if (vendas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nada para exportar.");
            return;
        }
        JFileChooser ch = new JFileChooser();
        ch.setSelectedFile(new File("relatorio-vendas.csv"));
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("ID;DataHora;Usuario;Forma;Placa;QtdItens;Total\n");
                for (Venda v : vendas) {
                    String dataStr = extrairDataHora(v);
                    String usuario = safe(v::getUsuario);
                    String forma = safe(v::getFormaPagamento);
                    String placa = safe(v::getPlaca);
                    BigDecimal total = safe(v::getTotal, BigDecimal.ZERO);
                    List<VendaItem> itens = safe(v::getItens, Collections.emptyList());
                    int qtdItens = itens != null ? itens.size() : 0;

                    String line = String.join(";",
                            String.valueOf(safe(v::getId)),
                            esc(dataStr),
                            esc(nvl(usuario)),
                            esc(nvl(forma)),
                            esc(nvl(placa)),
                            String.valueOf(qtdItens),
                            total.toPlainString()
                    );
                    bw.write(line);
                    bw.write("\n");
                }
                bw.flush();
                JOptionPane.showMessageDialog(this, "CSV exportado em: " + f.getAbsolutePath());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Falha ao exportar CSV: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirComprovanteSelecionado() {
        int row = tblVendas.getSelectedRow();
        if (row < 0 || row >= vendas.size()) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda primeiro.");
            return;
        }
        Venda v = vendas.get(row);
        try {
            Long id = safe(v::getId);
            if (id == null) {
                JOptionPane.showMessageDialog(this, "ID da venda não disponível.");
                return;
            }
            byte[] pdf = vendaService.baixarComprovante(id);
            Path tmp = Files.createTempFile("comprovante-" + id, ".pdf");
            Files.write(tmp, pdf);
            Desktop.getDesktop().open(tmp.toFile());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao abrir comprovante: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ======== UTILITÁRIOS ========
    private String extrairDataHora(Venda v) {
        // Se o seu model tiver LocalDateTime ou Date, ajuste aqui:
        try {
            OffsetDateTime d = v.getDataHora(); // ajuste se o getter tiver outro nome
            if (d != null) {
                return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(d);
            }
        } catch (Exception ignore) {}
        return "-";
    }

    private BigDecimal extrairPrecoUnitario(VendaItem it) {
        try { BigDecimal v = it.getPrecoUnitario(); if (v != null) return v; } catch (Exception ignore) {}
        try { BigDecimal v = it.getPrecoUnitario(); if (v != null) return v; } catch (Exception ignore) {}
        try { BigDecimal v = it.getPrecoUnitario(); if (v != null) return v; } catch (Exception ignore) {}
        return null;
    }

    private BigDecimal extrairSubtotal(VendaItem it) {
        try { BigDecimal v = it.getSubtotal(); if (v != null) return v; } catch (Exception ignore) {}
        try { BigDecimal v = it.getSubtotal(); if (v != null) return v; } catch (Exception ignore) {}
        return null;
    }

    private String esc(String s) {
        if (s == null) return "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private String nvl(String s) { return s == null ? "" : s; }

    @FunctionalInterface
    interface Caller<T> { T call() throws Exception; }
    private <T> T safe(Caller<T> c) {
        try { return c.call(); } catch (Exception e) { return null; }
    }
    private <T> T safe(Caller<T> c, T def) {
        try { T v = c.call(); return v == null ? def : v; } catch (Exception e) { return def; }
    }
}
