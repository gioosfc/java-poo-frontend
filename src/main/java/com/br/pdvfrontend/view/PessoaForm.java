package com.br.pdvfrontend.view;

import com.br.pdvfrontend.enums.TipoPessoa;
import com.br.pdvfrontend.model.Pessoa;
import com.br.pdvfrontend.service.PessoaService;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PessoaForm extends JDialog {

    // ---- Campos de UI ----
    private JTextField txtNome;
    private JTextField txtCpfCnpj;
    private JTextField txtEmail;
    private JSpinner   spnNumeroCtps;      // Number -> Long
    private JSpinner   spnDataNascimento;  // Date (mostrado como yyyy-MM-dd)
    private JComboBox<TipoPessoa> cbTipoPessoa;

    // ---- Dependências ----
    private final PessoaService pessoaService = new PessoaService();
    private Pessoa pessoa;
    private final PessoaList parent;

    public PessoaForm(Pessoa pessoa, PessoaList parent) {
        this.pessoa = pessoa;
        this.parent = parent;

        setTitle(pessoa == null ? "Nova Pessoa" : "Editar Pessoa");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Layout base
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        int row = 0;

        // Nome
        txtNome = new JTextField(25);
        addRow(content, gc, row++, "Nome completo:", txtNome);

        // CPF/CNPJ
        txtCpfCnpj = new JTextField(18);
        addRow(content, gc, row++, "CPF/CNPJ:", txtCpfCnpj);

        // Email (opcional)
        txtEmail = new JTextField(25);
        addRow(content, gc, row++, "Email (opcional):", txtEmail);

        // Número CTPS (opcional) - usa Integer no model
        spnNumeroCtps = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        JSpinner.NumberEditor numEditor = new JSpinner.NumberEditor(spnNumeroCtps, "#");
        spnNumeroCtps.setEditor(numEditor);

// Força inteiro e validação
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        intFormat.setGroupingUsed(false);
        NumberFormatter intFormatter = new NumberFormatter(intFormat);
        intFormatter.setValueClass(Integer.class);
        intFormatter.setMinimum(0);
        intFormatter.setMaximum(Integer.MAX_VALUE);
        intFormatter.setAllowsInvalid(false);
        intFormatter.setCommitsOnValidEdit(true);
        numEditor.getTextField().setFormatterFactory(new DefaultFormatterFactory(intFormatter));

        addRow(content, gc, row++, "Número CTPS (opcional):", spnNumeroCtps);


        // Data de Nascimento (obrigatório) - Date editor yyyy-MM-dd
        spnDataNascimento = new JSpinner(new SpinnerDateModel());
        spnDataNascimento.setEditor(new JSpinner.DateEditor(spnDataNascimento, "yyyy-MM-dd"));
        addRow(content, gc, row++, "Data de Nascimento:", spnDataNascimento);

        // Tipo de Pessoa (Enum) - obrigatório
        cbTipoPessoa = new JComboBox<>(new DefaultComboBoxModel<>(TipoPessoa.values()));
        addRow(content, gc, row++, "Tipo de Pessoa:", cbTipoPessoa);

        // Botões
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        pnlBotoes.add(btnCancelar);
        pnlBotoes.add(btnSalvar);

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 2;
        gc.weightx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.SOUTHEAST;
        content.add(pnlBotoes, gc);

        // Listeners
        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvar());

        // Pré-carregar dados se edição
        preencherSeEdicao();

        pack();
        setMinimumSize(new Dimension(480, getHeight()));
        setLocationRelativeTo(parent);
        setVisible(true);
    }


    private void preencherSeEdicao() {
        if (pessoa == null) {
            // criação: valores padrão (NÃO acessar pessoa.* aqui)
            if (cbTipoPessoa.getItemCount() > 0) cbTipoPessoa.setSelectedIndex(0);
            spnDataNascimento.setValue(new Date());
            spnNumeroCtps.setValue(0); // usamos Integer no Spinner
            return;
        }

        // edição: agora sim podemos ler pessoa.*
        txtNome.setText(pessoa.getNomeCompleto());
        txtCpfCnpj.setText(pessoa.getCpfCnpj());
        txtEmail.setText(pessoa.getEmail() == null ? "" : pessoa.getEmail());

        // Pessoa.getNumeroCtps() -> spinner (Integer), com clamp para evitar overflow
        int ctpsInt = (pessoa.getNumeroCtps() == null)
                ? 0
                : (int) Math.min(pessoa.getNumeroCtps(), (long) Integer.MAX_VALUE);
        spnNumeroCtps.setValue(ctpsInt);

        if (pessoa.getDataNascimento() != null) {
            Date d = Date.from(pessoa.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
            spnDataNascimento.setValue(d);
        } else {
            spnDataNascimento.setValue(new Date());
        }

        cbTipoPessoa.setSelectedItem(pessoa.getTipoPessoa());
    }

    private void salvar() {
        // 1) Crie o objeto antes de usar
        if (pessoa == null) {
            pessoa = new Pessoa();
        }

        // 2) Coleta e valida campos da UI (sem acessar pessoa.get... aqui)
        String nome = txtNome.getText().trim();
        String cpfCnpj = txtCpfCnpj.getText().trim();
        Date data = (Date) spnDataNascimento.getValue();
        TipoPessoa tipo = (TipoPessoa) cbTipoPessoa.getSelectedItem();

        if (nome.isEmpty()) { msgErro("Informe o nome completo."); return; }
        if (cpfCnpj.isEmpty()) { msgErro("Informe o CPF/CNPJ."); return; }
        if (data == null) { msgErro("Informe a data de nascimento."); return; }
        if (tipo == null) { msgErro("Selecione o tipo de pessoa."); return; }

        // 3) Preenche o modelo
        pessoa.setNomeCompleto(nome);
        pessoa.setCpfCnpj(cpfCnpj);

        String email = txtEmail.getText().trim();
        pessoa.setEmail(email.isEmpty() ? null : email);

        // Spinner usa Integer -> converta para Long com segurança
        Number ctpsNumber = (Number) spnNumeroCtps.getValue();
        Long ctps = (ctpsNumber == null) ? null : ctpsNumber.longValue();
        pessoa.setNumeroCtps((ctps != null && ctps > 0) ? ctps : null);

        LocalDate ld = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        pessoa.setDataNascimento(ld);

        pessoa.setTipoPessoa(tipo);

        // 4) Persistir
        try {
            pessoaService.salvar(pessoa);
            if (parent != null) parent.atualizarTabela();
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            msgErro("Falha ao salvar pessoa:\n" + ex.getMessage());
        }
    }

    private void msgErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    // Helper para adicionar linha (rótulo + componente)
    private void addRow(JPanel parent, GridBagConstraints base, int row, String label, JComponent comp) {
        GridBagConstraints gc = (GridBagConstraints) base.clone();

        gc.gridx = 0;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.weightx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.LINE_START;
        parent.add(new JLabel(label), gc);

        gc = (GridBagConstraints) base.clone();
        gc.gridx = 1;
        gc.gridy = row;
        gc.gridwidth = 1;
        gc.weightx = 1.0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        parent.add(comp, gc);
    }
}
