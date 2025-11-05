package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Contato;
import com.br.pdvfrontend.service.ContatoService;

import javax.swing.*;
import java.awt.*;

public class ContatoForm extends JDialog {

    private final ContatoList ownerList;
    private final ContatoService contatoService;
    private Contato contato; // objeto sendo editado ou null

    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;
    private JTextField txtNome;

    public ContatoForm(Frame owner, ContatoList ownerList, ContatoService contatoService, Contato contato) {
        this.ownerList = ownerList;
        this.contatoService = contatoService;
        this.contato = contato;

        setTitle(contato == null ? "Novo Contato" : "Editar Contato");
        setSize(450, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(20);
        formPanel.add(txtNome, gbc);

        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        txtTelefone = new JTextField(20);
        formPanel.add(txtTelefone, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Endereço
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField(20);
        formPanel.add(txtEndereco, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> onSalvar());
        btnCancelar.addActionListener(e -> dispose());

        if (contato != null) {
            carregarDados();
        }
    }

    private void carregarDados() {
        txtNome.setText(contato.getNome());
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
    }

    private void onSalvar() {
        String nome = txtNome.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome deve ser preenchido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criando contato novo
        if (contato == null) {
            contato = new Contato();
        }

        contato.setNome(nome);
        contato.setTelefone(telefone);
        contato.setEmail(email);
        contato.setEndereco(endereco);

        contatoService.salvar(contato);

        ownerList.atualizarTabela();

        JOptionPane.showMessageDialog(this, "Contato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }
}
