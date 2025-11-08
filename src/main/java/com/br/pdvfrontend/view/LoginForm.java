package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.model.SessaoUsuario;
import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de login do sistema PDV do posto de combustível.
 */
public class LoginForm extends JFrame {

    private final JTextField usuarioField;
    private final JPasswordField senhaField;
    private final AcessoService service = new AcessoService();

    public LoginForm() {
        setTitle("Login - PDV Posto de Combustível");
        setSize(320, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Campos =====
        add(new JLabel("Usuário:"));
        usuarioField = new JTextField();
        add(usuarioField);

        add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        add(senhaField);

        // ===== Botões =====
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> autenticar());
        add(btnEntrar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        add(btnSair);
    }

    /**
     * Realiza autenticação do usuário e abre a tela principal.
     */
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
                // Armazena informações na sessão
                SessaoUsuario.setUsuario(acesso.getUsuario());
                SessaoUsuario.setPapel(acesso.getPapel());

                // Fecha a tela de login
                this.dispose();

                // ✅ Abre o menu principal completo (com PDV, cadastros, etc.)
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

    // ====== MAIN opcional (para teste isolado) ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
