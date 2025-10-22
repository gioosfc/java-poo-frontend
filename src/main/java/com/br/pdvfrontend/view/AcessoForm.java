package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;

import javax.swing.*;
import java.awt.*;

public class AcessoForm extends JDialog {

    private final AcessoList ownerList;
    private Acesso acesso; // O objeto sendo editado, ou null se for novo

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public AcessoForm(Frame owner, AcessoList ownerList, Acesso acesso) {
        super(owner, (acesso == null) ? "Novo Acesso" : "Editar Acesso", true);
        this.ownerList = ownerList;
        this.acesso = acesso;

        // --- Configurações da Janela ---
        setSize(400, 200);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Componentes ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(20);
        formPanel.add(txtUsuario, gbc);

        // Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(20);
        formPanel.add(txtSenha, gbc);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Ações ---
        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        // Carrega dados se estiver em modo de edição
        if (acesso != null) {
            txtUsuario.setText(acesso.getUsuario());
            txtSenha.setText(acesso.getSenha());
            txtUsuario.setEditable(false); // Não permite editar o nome de usuário
        }
    }

    private void onSalvar() {
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuário e Senha são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se o objeto `acesso` é nulo, cria um novo
        if (acesso == null) {
            acesso = new Acesso(usuario, senha);
        } else {
            // Se já existe, apenas atualiza a senha
            acesso.setSenha(senha);
        }

        // Chama o método da tela de lista para salvar o objeto
        ownerList.salvarAcesso(acesso);

        JOptionPane.showMessageDialog(this, "Acesso salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        // Fecha o formulário
        dispose();
    }
}
