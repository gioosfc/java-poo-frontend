package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Contato;
import com.br.pdvfrontend.service.ContatoService;

import javax.swing.*;
import java.awt.*;

public class ContatoForm extends JDialog {

    private final ContatoList ownerList;
    private final ContatoService contatoService;
    private Contato contato; // O objeto sendo editado, ou null se for novo

    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtEndereco;

    public ContatoForm(Frame owner, ContatoList ownerList, ContatoService contatoService, Contato contato) {
        super(owner, (contato == null) ? "Novo Contato" : "Editar Contato", true);
        this.ownerList = ownerList;
        this.contatoService = contatoService;
        this.contato = contato;

        // --- Configurações da Janela ---
        setSize(450, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // --- Componentes ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campo Telefone
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        txtTelefone = new JTextField(20);
        formPanel.add(txtTelefone, gbc);

        // Campo Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        // Campo Endereço
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        txtEndereco = new JTextField(20);
        formPanel.add(txtEndereco, gbc);

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
        if (contato != null) {
            carregarDados();
        }
    }

    private void carregarDados() {
        txtTelefone.setText(contato.getTelefone());
        txtEmail.setText(contato.getEmail());
        txtEndereco.setText(contato.getEndereco());
    }

    private void onSalvar() {
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String endereco = txtEndereco.getText().trim();

        if (telefone.isEmpty() && email.isEmpty() && endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pelo menos um campo deve ser preenchido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Se o objeto `contato` é nulo, cria um novo
        if (contato == null) {
            contato = new Contato();
        }

        // Preenche o objeto com os dados da tela
        contato.setTelefone(telefone);
        contato.setEmail(email);
        contato.setEndereco(endereco);

        // Salva o objeto usando o serviço
        contatoService.salvar(contato);

        // Atualiza a tabela na tela de lista
        ownerList.atualizarTabela();

        JOptionPane.showMessageDialog(this, "Contato salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        // Fecha o formulário
        dispose();
    }
}
