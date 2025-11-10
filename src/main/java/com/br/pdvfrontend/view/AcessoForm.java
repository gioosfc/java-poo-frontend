package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Acesso;
import com.br.pdvfrontend.service.AcessoService;
import com.formdev.flatlaf.extras.FlatSVGIcon;

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
        super(owner, (acesso == null ? "Novo Acesso" : "Editar Acesso"), true);
        this.listScreen = listScreen;
        this.service = service;
        this.acesso = acesso;

        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Painel principal com GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Usuário
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        panel.add(txtUsuario, gbc);

        // Campo Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        panel.add(txtSenha, gbc);

        // Campo Papel
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Papel:"), gbc);
        gbc.gridx = 1;
        cbPapel = new JComboBox<>(new String[]{"ADMIN", "OPERADOR"});
        panel.add(cbPapel, gbc);

        add(panel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setIcon(new FlatSVGIcon("icons/save.svg"));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setIcon(new FlatSVGIcon("icons/cancel.svg"));
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

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

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Usuário é obrigatório!");
            return;
        }

        // A senha só é obrigatória para novos usuários
        if (acesso == null && senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Senha é obrigatório para novos usuários!");
            return;
        }

        try {
            if (acesso == null) {
                acesso = new Acesso(null, usuario, senha, papel);
            } else {
                acesso.setUsuario(usuario);
                // Só atualiza a senha se uma nova for digitada
                if (!senha.isEmpty()) {
                    acesso.setSenha(senha);
                }
                acesso.setPapel(papel);
            }

            service.salvar(acesso);
            JOptionPane.showMessageDialog(this, "Acesso salvo com sucesso!");

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
