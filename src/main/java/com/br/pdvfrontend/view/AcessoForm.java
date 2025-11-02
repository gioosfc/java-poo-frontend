package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import java.awt.*;

public class AcessoForm extends JDialog {

    private AcessoList listScreen = null;
    private final AcessoService service;
    private Acesso acesso;

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public AcessoForm(Frame owner, AcessoService service, Acesso acesso) {
        super(owner, "Cadastro de Acesso", true);
        this.listScreen = listScreen;
        this.service = service;
        this.acesso = acesso;

        setSize(300, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(5, 5));

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        panel.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);

        add(panel, BorderLayout.CENTER);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSalvar());
        add(btnSalvar, BorderLayout.SOUTH);

        if (acesso != null) {
            txtUsuario.setText(acesso.getUsuario());
            txtSenha.setText(acesso.getSenha());
        }
    }

    private void onSalvar() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuário e Senha são obrigatórios!");
            return;
        }

        if (acesso == null) {
            acesso = new Acesso(null, usuario, senha);
            service.salvar(acesso);
            JOptionPane.showMessageDialog(this, "Cadastrado com sucesso!");
        } else {
            acesso.setUsuario(usuario);
            acesso.setSenha(senha);
            service.atualizar(acesso.getId(), acesso);
            JOptionPane.showMessageDialog(this, "Alterado com sucesso!");
        }

        listScreen.atualizarTabela();
        dispose();
    }
}
