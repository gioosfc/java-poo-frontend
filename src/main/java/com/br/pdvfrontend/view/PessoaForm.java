package com.br.pdvfrontend.view;

import com.br.pdvfrontend.model.Pessoa;
import com.br.pdvfrontend.service.PessoaService;
import javax.swing.*;
import java.awt.*;

public class PessoaForm extends JDialog {

    private JTextField txtNome, txtCpf, txtEmail;
    private PessoaService pessoaService = new PessoaService();
    private Pessoa pessoa;
    private PessoaList parent;

    public PessoaForm(Pessoa pessoa, PessoaList parent) {
        this.pessoa = pessoa;
        this.parent = parent;

        setTitle(pessoa == null ? "Nova Pessoa" : "Editar Pessoa");
        setSize(400, 250);
        setLayout(new GridLayout(5, 2, 10, 10)); // Adicionado espaçamento
        setLocationRelativeTo(parent);
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona margem

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        add(txtCpf);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        // Espaçadores para empurrar o botão para baixo
        add(new JLabel());
        add(new JLabel());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        add(btnSalvar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        add(btnCancelar);

        if (pessoa != null) {
            txtNome.setText(pessoa.getNome());
            txtCpf.setText(pessoa.getCpf());
            txtEmail.setText(pessoa.getEmail());
        }

        setModal(true);
        setVisible(true);
    }

    private void salvar() {
        if (pessoa == null) {
            pessoa = new Pessoa();
        }

        pessoa.setNome(txtNome.getText());
        pessoa.setCpf(txtCpf.getText());
        pessoa.setEmail(txtEmail.getText());

        pessoaService.salvar(pessoa);

        parent.atualizarTabela();
        dispose();
    }
}