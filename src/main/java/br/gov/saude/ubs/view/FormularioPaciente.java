package br.gov.saude.ubs.view;

import javax.swing.*;
import java.awt.*;
import br.gov.saude.ubs.model.Paciente;
import br.gov.saude.ubs.repository.PacienteRepository;
import br.gov.saude.ubs.util.MascaraUtil;
import br.gov.saude.ubs.config.LogConfig;

public class FormularioPaciente extends JDialog {
    // Campos de Dados Pessoais
    private JTextField txtNome, txtCpf, txtCns, txtNascimento, txtMae;
    // Campos de Endereço
    private JTextField txtRua, txtNum, txtBairro, txtCidade, txtComp;
    // Campos de Telefone
    private JTextField txtTel1, txtTel2, txtTel3;
    
    private PacienteRepository repo = new PacienteRepository();
    private MainFrame parent;

    public FormularioPaciente(MainFrame parent) {
        super(parent, "Novo Cadastro de Paciente", true);
        this.parent = parent;
        setup();
    }

    private void setup() {
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        
        // Aba 1: Dados Pessoais
        JPanel pnlDados = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlDados.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlDados.add(new JLabel("Nome Completo:")); txtNome = new JTextField(); pnlDados.add(txtNome);
        pnlDados.add(new JLabel("CPF:")); txtCpf = new JTextField(); pnlDados.add(txtCpf);
        pnlDados.add(new JLabel("CNS:")); txtCns = new JTextField(); pnlDados.add(txtCns);
        pnlDados.add(new JLabel("Data Nasc (DD/MM/AAAA):")); txtNascimento = new JFormattedTextField(MascaraUtil.aplicar("##/##/####")); pnlDados.add(txtNascimento); JLabel lblIdade = new JLabel("Idade: -- anos");
        pnlDados.add(new JLabel("Nome da Mãe:")); txtMae = new JTextField(); pnlDados.add(txtMae);
        
        txtNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
            calcularIdade(txtNascimento.getText(), lblIdade);
        }
        });

        // Aba 2: Endereço
        JPanel pnlEndereco = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlEndereco.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlEndereco.add(new JLabel("Rua/Logradouro:")); txtRua = new JTextField(); pnlEndereco.add(txtRua);
        pnlEndereco.add(new JLabel("Número:")); txtNum = new JTextField(); pnlEndereco.add(txtNum);
        pnlEndereco.add(new JLabel("Bairro:")); txtBairro = new JTextField(); pnlEndereco.add(txtBairro);
        pnlEndereco.add(new JLabel("Cidade:")); txtCidade = new JTextField("Ourinhos"); pnlEndereco.add(txtCidade);
        pnlEndereco.add(new JLabel("Complemento:")); txtComp = new JTextField(); pnlEndereco.add(txtComp);

        // Aba 3: Contatos
        JPanel pnlContato = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlContato.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlContato.add(new JLabel("Celular (Principal):")); txtTel1 = new JTextField(); pnlContato.add(txtTel1);
        pnlContato.add(new JLabel("Telefone (Recado):")); txtTel2 = new JTextField(); pnlContato.add(txtTel2);
        pnlContato.add(new JLabel("Telefone Fixo:")); txtTel3 = new JTextField(); pnlContato.add(txtTel3);

        abas.addTab("Identificação", pnlDados);
        abas.addTab("Localização", pnlEndereco);
        abas.addTab("Contatos", pnlContato);
        add(abas, BorderLayout.CENTER);

        // Botão Salvar
        JButton btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(0, 153, 76));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> salvar());
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvar() {
        if (txtNome.getText().trim().isEmpty() || txtCns.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CNS são obrigatórios para a UBS.");
            return;
        }

        Paciente p = new Paciente();
        p.setNome(txtNome.getText());
        p.setCpf(txtCpf.getText());
        p.setCns(txtCns.getText());
        p.setDataNascimento(MascaraUtil.paraISO(txtNascimento.getText()));
        p.setNomeMae(txtMae.getText());
        p.setLogradouro(txtRua.getText());
        p.setNumero(txtNum.getText());
        p.setBairro(txtBairro.getText());
        p.setCidade(txtCidade.getText());
        p.setComplemento(txtComp.getText());
        p.setTelPrincipal(txtTel1.getText());
        p.setTelSecundario(txtTel2.getText());
        p.setTelFixo(txtTel3.getText());

        if (repo.salvar(p)) {
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            LogConfig.info("Novo paciente cadastrado via formulário: " + p.getNome());
            parent.atualizarTabela(); // Atualiza a lista no MainFrame
            dispose(); // Fecha o formulário
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar. Verifique se o CPF/CNS já existe.");
        }
    }

    private void calcularIdade(String dataStr, JLabel label) {
        try {
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate nasc = java.time.LocalDate.parse(dataStr, fmt);
            java.time.LocalDate hoje = java.time.LocalDate.now();
            long idade = java.time.temporal.ChronoUnit.YEARS.between(nasc, hoje);
            label.setText("Idade: " + idade + " anos");
        } catch (Exception e) {
            label.setText("Idade: -- anos");
        }
    }
}