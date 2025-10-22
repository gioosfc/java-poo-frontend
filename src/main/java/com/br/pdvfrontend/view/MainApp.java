package com.br.pdvfrontend.view;

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {
        // Garante que a UI seja criada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Usa o Look and Feel do sistema operacional para uma aparência nativa
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Cria a janela principal da aplicação
        JFrame mainFrame = new JFrame("PDV - Principal");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // Cria a barra de menus
        JMenuBar menuBar = new JMenuBar();

        // --- Menu "Cadastros" ---
        JMenu cadastrosMenu = new JMenu("Cadastros");
        menuBar.add(cadastrosMenu);

        // Adiciona um item de menu para cada entidade
        addMenuItem(cadastrosMenu, "Pessoas", () -> new PessoaList().setVisible(true));
        addMenuItem(cadastrosMenu, "Acessos", () -> new AcessoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Contatos", () -> new ContatoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Custos", () -> new CustoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Produtos", () -> new ProdutoList().setVisible(true));
        addMenuItem(cadastrosMenu, "Estoques", () -> new EstoqueList().setVisible(true));
        addMenuItem(cadastrosMenu, "Preços", () -> new PrecoList().setVisible(true));

        // Define a barra de menus na janela
        mainFrame.setJMenuBar(menuBar);

        // Painel de boas-vindas
        JLabel welcomeLabel = new JLabel("Selecione uma opção no menu Cadastros.", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainFrame.add(welcomeLabel, BorderLayout.CENTER);

        // Exibe a janela
        mainFrame.setVisible(true);
    }

    /**
     * Método utilitário para criar e adicionar um JMenuItem a um JMenu.
     */
    private static void addMenuItem(JMenu menu, String itemName, Runnable action) {
        JMenuItem menuItem = new JMenuItem(itemName);
        menuItem.addActionListener(e -> action.run());
        menu.add(menuItem);
    }
}
