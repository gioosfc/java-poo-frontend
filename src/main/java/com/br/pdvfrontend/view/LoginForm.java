package com.br.pdvfrontend.view;

import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private JTextField usuarioField;
    private JPasswordField senhaField;
    private final AcessoService service = new AcessoService();

    public LoginForm() {
        setTitle("Login");
        setSize(300, 170);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setResizable(false);

        add(new JLabel("Usuário:"));
        usuarioField = new JTextField();
        add(usuarioField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.addActionListener(e -> autenticar());
        add(btnLogin);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        add(btnSair);
    }

    private void autenticar() {
        String usuario = usuarioField.getText();
        String senha = new String(senhaField.getPassword());

        if (service.login(usuario, senha)) {
            dispose(); // fecha o login
            MainApp.createAndShowGUI(); // abre o principal
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!");
        }
    }
}
