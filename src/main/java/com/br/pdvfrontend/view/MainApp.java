package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.SessaoUsuario;
import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainApp {

    public static void main(String[] args) {
        // Configura o Look and Feel FlatLaf
        FlatDarculaLaf.setup();

        // Define cores personalizadas
        UIManager.put("Button.arc", 10);
        UIManager.put("Component.arc", 10);
        UIManager.put("ProgressBar.arc", 10);
        UIManager.put("TextComponent.arc", 10);

        // Cor verde para botões de confirmação
        UIManager.put("Button.background", new Color(46, 139, 87));
        UIManager.put("Button.foreground", Color.WHITE);

        // Cor vermelha para botões de exclusão
        UIManager.put("Button.toolbar.background", new Color(178, 34, 34));
        UIManager.put("Button.toolbar.foreground", Color.WHITE);

        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }

    public static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("AbasteceMais");

        // 🔹 Adiciona ícone da aplicação
        //var a = MainApp.class.getResource("C:\\frontend\\java-poo-frontend\\target\\classes\\images\\abastecemais_logo.png");
        //ImageIcon logoIcon = new ImageIcon(a);
        //mainFrame.setIconImage(logoIcon.getImage());

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

        addMenuItem(cadastrosMenu, "Pessoas", "control E", () -> new PessoaList().setVisible(true));
        addMenuItem(cadastrosMenu, "Acessos", "control A", () -> new AcessoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Contatos", null, () -> new ContatoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Custos", null, () -> new CustoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Produtos", "control P", () -> new ProdutoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Estoques", null, () -> new EstoqueList().setVisible(true));
        addMenuItem(cadastrosMenu, "Preços", null, () -> new PrecoList().setVisible(true));

        menuBar.add(cadastrosMenu);

        // ---- Relatórios ----
        JMenu relatoriosMenu = new JMenu("Relatórios");
        addMenuItem(relatoriosMenu, "Vendas do Dia", null, () -> new RelatorioVendasForm(mainFrame).setVisible(true));
        menuBar.add(relatoriosMenu);

        // ---- Sistema ----
        JMenu sistemaMenu = new JMenu("Sistema");

        addMenuItem(sistemaMenu, "Trocar Usuário", null, () -> {
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

        addMenuItem(sistemaMenu, "Sair", "alt F4", () -> {
            int confirm = JOptionPane.showConfirmDialog(
                    mainFrame,
                    "Deseja realmente sair do sistema?",
                    "Encerrar AbasteceMais",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuBar.add(sistemaMenu);

        // ---- Restrições por papel ----
        if ("OPERADOR".equalsIgnoreCase(SessaoUsuario.getPapel())) {
            // Acesso, Pessoas, Custos
            cadastrosMenu.getMenuComponent(1).setEnabled(false);
            cadastrosMenu.getMenuComponent(0).setEnabled(false);
            cadastrosMenu.getMenuComponent(3).setEnabled(false);
        }

        mainFrame.setJMenuBar(menuBar);

        // ======================= CONTEÚDO CENTRAL =======================
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 🔹 Adiciona logo acima da mensagem de boas-vindas
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //var b = MainApp.class.getResource("C:\\frontend\\java-poo-frontend\\target\\classes\\images\\abastecemais_logo.png");
        //logoLabel.setIcon(new ImageIcon(b));
        contentPanel.add(logoLabel, BorderLayout.NORTH);

        JLabel welcomeLabel = new JLabel("Bem-vindo ao AbasteceMais", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Inter", Font.BOLD, 28));

        JLabel hintLabel = new JLabel("Use o menu para navegar.", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Inter", Font.PLAIN, 18));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(welcomeLabel);
        centerPanel.add(hintLabel);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // ======================= BARRA INFERIOR (STATUS) =======================
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel userLabel = new JLabel("Usuário: " + SessaoUsuario.getUsuario() +
                "  |  Perfil: " + SessaoUsuario.getPapel());
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        JLabel clockLabel = new JLabel();
        clockLabel.setFont(new Font("Inter", Font.PLAIN, 14));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            @Override
            public void run() {
                clockLabel.setText(sdf.format(new Date()));
            }
        }, 0, 1000);

        statusBar.add(userLabel, BorderLayout.WEST);
        statusBar.add(clockLabel, BorderLayout.EAST);

        contentPanel.add(statusBar, BorderLayout.SOUTH);

        mainFrame.add(contentPanel);
        mainFrame.setVisible(true);
    }

    private static void addMenuItem(JMenu menu, String text, String accelerator, Runnable action) {
        JMenuItem menuItem = new JMenuItem(text);
        if (accelerator != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        menuItem.addActionListener(e -> action.run());
        menu.add(menuItem);
    }
}
