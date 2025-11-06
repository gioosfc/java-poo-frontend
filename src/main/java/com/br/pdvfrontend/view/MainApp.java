package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.SessaoUsuario;
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

            new LoginForm().setVisible(true);
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

        // ---- Criando menus individuais ----
        JMenuItem pessoasMenu  = new JMenuItem("Pessoas");
        pessoasMenu.addActionListener(e -> new PessoaList().setVisible(true));
        cadastrosMenu.add(pessoasMenu);

        JMenuItem acessoMenu = new JMenuItem("Acessos");
        acessoMenu.addActionListener(e -> new AcessoList().setVisible(true));
        cadastrosMenu.add(acessoMenu);

        JMenuItem contatoMenu = new JMenuItem("Contatos");
        contatoMenu.addActionListener(e -> new ContatoList().setVisible(true));
        cadastrosMenu.add(contatoMenu);

        JMenuItem custoMenu = new JMenuItem("Custos");
        custoMenu.addActionListener(e -> new CustoList().setVisible(true));
        cadastrosMenu.add(custoMenu);

        JMenuItem produtoMenu = new JMenuItem("Produtos");
        produtoMenu.addActionListener(e -> new ProdutoList().setVisible(true));
        cadastrosMenu.add(produtoMenu);

        JMenuItem estoqueMenu = new JMenuItem("Estoques");
        estoqueMenu.addActionListener(e -> new EstoqueList().setVisible(true));
        cadastrosMenu.add(estoqueMenu);

        JMenuItem precoMenu = new JMenuItem("Preços");
        precoMenu.addActionListener(e -> new PrecoList().setVisible(true));
        cadastrosMenu.add(precoMenu);

        // ---- BLOQUEIOS DE ACESSO ----
        if ("OPERADOR".equalsIgnoreCase(SessaoUsuario.papel)) {
            acessoMenu.setEnabled(false);
            pessoasMenu.setEnabled(false);
        }

        mainFrame.setJMenuBar(menuBar);

        // Mensagem central
        JLabel welcomeLabel = new JLabel("Selecione uma opção no menu.", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mainFrame.add(welcomeLabel, BorderLayout.CENTER);

        // ✅ Mostra usuário logado
        JLabel userLabel = new JLabel(
                "Usuário logado: " + SessaoUsuario.usuario +
                        " (" + SessaoUsuario.papel + ")",
                SwingConstants.RIGHT
        );
        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainFrame.add(userLabel, BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }
}
