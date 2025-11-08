package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.service.AcessoService;

import javax.swing.*;
import java.awt.*;

public class AcessoForm extends JDialog {

    private final AcessoList listScreen;
    private final AcessoService service;
    private Acesso acesso;

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPapel;

    public AcessoForm(Frame owner, AcessoService service, Acesso acesso, AcessoList listScreen) {
        super(owner, "Cadastro de Acesso", true);
        this.listScreen = listScreen;
        this.service = service;
        this.acesso = acesso;

        setSize(350, 220);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(5, 5));

        // Painel principal com os campos
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        // Campo Usuário
        panel.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);

        // Campo Senha
        panel.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        panel.add(txtSenha);

        // Campo Papel
        panel.add(new JLabel("Papel:"));
        cbPapel = new JComboBox<>(new String[]{"ADMIN", "USER"});
        panel.add(cbPapel);

        add(panel, BorderLayout.CENTER);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSalvar());
        add(btnSalvar, BorderLayout.SOUTH);

        // Se for edição, preencher os campos
        if (acesso != null) {
            txtUsuario.setText(acesso.getUsuario());
            txtSenha.setText(acesso.getSenha());
            if (acesso.getPapel() != null) {
                cbPapel.setSelectedItem(acesso.getPapel());
            }
        }
    }

    private void onSalvar() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
        String papel = (String) cbPapel.getSelectedItem();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuário e Senha são obrigatórios!");
            return;
        }

        try {
            if (acesso == null) {
                acesso = new Acesso(null, usuario, senha, papel);
                service.salvar(acesso);
                JOptionPane.showMessageDialog(this, "Cadastrado com sucesso!");
            } else {
                acesso.setUsuario(usuario);
                acesso.setSenha(senha);
                acesso.setPapel(papel);
                service.atualizar(acesso.getId(), acesso);
                JOptionPane.showMessageDialog(this, "Alterado com sucesso!");
            }

            listScreen.atualizarTabela();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar o acesso: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
