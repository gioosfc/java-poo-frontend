package com.br.pdvfrontend.view;

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new LoginForm().setVisible(true); // <-- abre o login primeiro
        });
    }

    public static void createAndShowGUI() {

        JFrame mainFrame = new JFrame("PDV - Principal");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu cadastrosMenu = new JMenu("Cadastros");
        menuBar.add(cadastrosMenu);

        addMenuItem(cadastrosMenu, "Pessoas", () -> new PessoaList().setVisible(true));
        addMenuItem(cadastrosMenu, "Acessos", () -> new AcessoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Contatos", () -> new ContatoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Custos", () -> new CustoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Produtos", () -> new ProdutoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Estoques", () -> new EstoqueList().setVisible(true));
        addMenuItem(cadastrosMenu, "Preços", () -> new PrecoList().setVisible(true));

        mainFrame.setJMenuBar(menuBar);

        JLabel welcomeLabel = new JLabel("Selecione uma opção no menu Cadastros.", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainFrame.add(welcomeLabel, BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private static void addMenuItem(JMenu menu, String itemName, Runnable action) {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(e -> action.run());
        menu.add(menuItem);
    }
}
