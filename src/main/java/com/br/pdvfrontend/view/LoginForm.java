package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.model.SessaoUsuario;
import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private final JTextField usuarioField;
    private final JPasswordField senhaField;
    private final AcessoService service = new AcessoService();

    public LoginForm() {
        setTitle("Login - AbasteceMais");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel principal com GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("AbasteceMais", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel.add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 1;

        // Campo Usuário
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        usuarioField = new JTextField(15);
        panel.add(usuarioField, gbc);

        // Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        senhaField = new JPasswordField(15);
        panel.add(senhaField, gbc);

        // Botão Entrar
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> autenticar());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(btnEntrar, gbc);

        add(panel);
    }

    private void autenticar() {
        String usuario = usuarioField.getText().trim();
        String senha = new String(senhaField.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe usuário e senha!");
            return;
        }

        try {
            Acesso acesso = service.login(usuario, senha);

            if (acesso != null) {
                SessaoUsuario.setUsuario(acesso.getUsuario());
                SessaoUsuario.setPapel(acesso.getPapel());
                this.dispose();
                MainApp.createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao autenticar: " + ex.getMessage(),
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
