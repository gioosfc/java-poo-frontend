package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.SessaoUsuario;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Tela principal do sistema PDV - Posto de Combustível.
 * Exibe menus de navegação e informações do operador logado.
 */
public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginForm().setVisible(true);
        });
    }

    public static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("PDV - Posto de Combustível");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);

        // ======================= MENU BAR =======================
        JMenuBar menuBar = new JMenuBar();

        // ---- PDV ----
        JMenu pdvMenu = new JMenu("PDV");
        pdvMenu.setMnemonic('P');

        JMenuItem novaVenda = new JMenuItem("Nova Venda");
        novaVenda.setAccelerator(KeyStroke.getKeyStroke("control N"));
        novaVenda.addActionListener(e -> new VendaForm(mainFrame).setVisible(true));
        pdvMenu.add(novaVenda);

        menuBar.add(pdvMenu);

        // ---- Cadastros ----
        JMenu cadastrosMenu = new JMenu("Cadastros");
        cadastrosMenu.setMnemonic('C');

        JMenuItem pessoasMenu = new JMenuItem("Pessoas");
        pessoasMenu.setAccelerator(KeyStroke.getKeyStroke("control E"));
        pessoasMenu.addActionListener(e -> new PessoaList().setVisible(true));
        cadastrosMenu.add(pessoasMenu);

        JMenuItem acessoMenu = new JMenuItem("Acessos");
        acessoMenu.setAccelerator(KeyStroke.getKeyStroke("control A"));
        acessoMenu.addActionListener(e -> new AcessoList().setVisible(true));
        cadastrosMenu.add(acessoMenu);

        JMenuItem contatoMenu = new JMenuItem("Contatos");
        contatoMenu.addActionListener(e -> new ContatoList().setVisible(true));
        cadastrosMenu.add(contatoMenu);

        JMenuItem custoMenu = new JMenuItem("Custos");
        custoMenu.addActionListener(e -> new CustoList().setVisible(true));
        cadastrosMenu.add(custoMenu);

        JMenuItem produtoMenu = new JMenuItem("Produtos");
        produtoMenu.setAccelerator(KeyStroke.getKeyStroke("control P"));
        produtoMenu.addActionListener(e -> new ProdutoList().setVisible(true));
        cadastrosMenu.add(produtoMenu);

        JMenuItem estoqueMenu = new JMenuItem("Estoques");
        estoqueMenu.addActionListener(e -> new EstoqueList().setVisible(true));
        cadastrosMenu.add(estoqueMenu);

        JMenuItem precoMenu = new JMenuItem("Preços");
        precoMenu.addActionListener(e -> new PrecoList().setVisible(true));
        cadastrosMenu.add(precoMenu);

        menuBar.add(cadastrosMenu);

        // ---- Relatórios ----
        JMenu relatoriosMenu = new JMenu("Relatórios");
        JMenuItem vendasHoje = new JMenuItem("Vendas do Dia");
        // vendasHoje.addActionListener(e -> new RelatorioVendasForm().setVisible(true));
        relatoriosMenu.add(vendasHoje);
        menuBar.add(relatoriosMenu);

        // ---- Sistema ----
        JMenu sistemaMenu = new JMenu("Sistema");

        JMenuItem logout = new JMenuItem("Trocar Usuário");
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Deseja sair e trocar de usuário?",
                    "Trocar Usuário",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                mainFrame.dispose();
                new LoginForm().setVisible(true);
            }
        });
        sistemaMenu.add(logout);

        JMenuItem sairMenu = new JMenuItem("Sair");
        sairMenu.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        sairMenu.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Deseja realmente sair do sistema?",
                    "Encerrar PDV",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        sistemaMenu.add(sairMenu);

        menuBar.add(sistemaMenu);

        // ---- Restrições por papel ----
        if ("OPERADOR".equalsIgnoreCase(SessaoUsuario.getPapel())) {
            acessoMenu.setEnabled(false);
            pessoasMenu.setEnabled(false);
            custoMenu.setEnabled(false);
        }

        mainFrame.setJMenuBar(menuBar);

        // ======================= CONTEÚDO CENTRAL =======================
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));

        JLabel welcomeLabel = new JLabel("🚀 Bem-vindo ao Sistema PDV do Posto de Combustível", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(33, 33, 33));

        JLabel hintLabel = new JLabel("Use o menu superior para acessar cadastros, PDV e relatórios.", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(welcomeLabel);
        centerPanel.add(hintLabel);
        centerPanel.setBackground(Color.WHITE);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // ======================= BARRA INFERIOR (STATUS) =======================
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(230, 230, 230));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel userLabel = new JLabel("👤 Usuário: " + SessaoUsuario.getUsuario() +
                "  |  Perfil: " + SessaoUsuario.getPapel());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Relógio em tempo real
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            @Override
            public void run() {
                clockLabel.setText("🕒 " + sdf.format(new Date()));
            }
        }, 0, 1000);

        statusBar.add(userLabel, BorderLayout.WEST);
        statusBar.add(clockLabel, BorderLayout.EAST);

        contentPanel.add(statusBar, BorderLayout.SOUTH);

        mainFrame.add(contentPanel);
        mainFrame.setVisible(true);
    }
}
