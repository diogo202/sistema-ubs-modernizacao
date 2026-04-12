package br.gov.saude.ubs.view;

import javax.swing.*;
import java.awt.*;
import br.gov.saude.ubs.model.Paciente;
import br.gov.saude.ubs.repository.PacienteRepository;
import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.util.MascaraUtil;

public class FormularioPaciente extends JDialog {
    // ATRIBUTOS (Onde estava o erro: faltava declarar o txtCep aqui)
    private JTextField txtNome, txtMae, txtRua, txtNum, txtBairro, txtCidade, txtComp;
    private JFormattedTextField txtCpf, txtCns, txtNascimento, txtCep, txtTel1, txtTel2, txtTel3;
    
    private PacienteRepository repo = new PacienteRepository();
    private MainFrame parent;

    public FormularioPaciente(MainFrame parent) {
        super(parent, "Novo Cadastro de Paciente", true);
        this.parent = parent;
        setup();
    }

    private void setup() {
        setSize(550, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        
        // Aba 1: Dados Pessoais
        JPanel pnlDados = new JPanel(new GridLayout(6, 2, 10, 10)); // Aumentei para 6 linhas
        pnlDados.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlDados.add(new JLabel("Nome Completo:")); 
        txtNome = new JTextField(); pnlDados.add(txtNome);
        
        pnlDados.add(new JLabel("CPF:")); 
        txtCpf = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CPF)); pnlDados.add(txtCpf);
        
        pnlDados.add(new JLabel("CNS:")); 
        txtCns = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CNS)); pnlDados.add(txtCns);
        
        pnlDados.add(new JLabel("Data Nasc (DD/MM/AAAA):")); 
        txtNascimento = new JFormattedTextField(MascaraUtil.aplicar("##/##/####")); pnlDados.add(txtNascimento); JLabel lblIdade = new JLabel("Idade: -- anos"); pnlDados.add(lblIdade);
        JLabel lblExibirIdade = new JLabel("Idade: --");
        lblExibirIdade.setForeground(new Color(0, 102, 204));
        lblExibirIdade.setFont(new Font("Arial", Font.BOLD, 12));

        txtNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            String dataStr = txtNascimento.getText().trim();
            if (dataStr.length() == 10 && !dataStr.contains("_")) {
                try {
                    java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    java.time.LocalDate nasc = java.time.LocalDate.parse(dataStr, fmt);
                    long idade = java.time.temporal.ChronoUnit.YEARS.between(nasc, java.time.LocalDate.now());
                    lblExibirIdade.setText("Idade: " + idade + " anos");
                    } catch (Exception e) {
                        lblExibirIdade.setText("Data Inválida");
                    }
                }
            }
        });

        // Adicione o label ao painel (ajuste o GridLayout para 7 linhas se necessário)
        pnlDados.add(new JLabel("Cálculo Automático:"));
        pnlDados.add(lblExibirIdade);

        pnlDados.add(new JLabel("Nome da Mãe:")); 
        txtMae = new JTextField(); pnlDados.add(txtMae);
        
        // Aba 2: Localização
        JPanel pnlEndereco = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlEndereco.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlEndereco.add(new JLabel("CEP:")); 
        txtCep = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CEP)); pnlEndereco.add(txtCep);
        
        pnlEndereco.add(new JLabel("Rua/Logradouro:")); 
        txtRua = new JTextField(); pnlEndereco.add(txtRua);
        
        pnlEndereco.add(new JLabel("Número:")); 
        txtNum = new JTextField(); pnlEndereco.add(txtNum);
        
        pnlEndereco.add(new JLabel("Bairro:")); 
        txtBairro = new JTextField(); pnlEndereco.add(txtBairro);
        
        pnlEndereco.add(new JLabel("Cidade:")); 
        txtCidade = new JTextField("Ourinhos"); pnlEndereco.add(txtCidade);
        
        pnlEndereco.add(new JLabel("Complemento:")); 
        txtComp = new JTextField(); pnlEndereco.add(txtComp);

        // Aba 3: Contatos
        JPanel pnlContato = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlContato.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        pnlContato.add(new JLabel("Celular:")); 
        txtTel1 = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CELULAR)); pnlContato.add(txtTel1);
        
        pnlContato.add(new JLabel("Telefone Recado:")); 
        txtTel2 = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CELULAR)); pnlContato.add(txtTel2);
        
        pnlContato.add(new JLabel("Telefone Fixo:")); 
        txtTel3 = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_FIXO)); pnlContato.add(txtTel3);

        abas.addTab("Identificação", pnlDados);
        abas.addTab("Localização", pnlEndereco);
        abas.addTab("Contatos", pnlContato);
        add(abas, BorderLayout.CENTER);

        JButton btnSalvar = new JButton("Salvar Cadastro");
        btnSalvar.addActionListener(e -> salvarAcao());
        add(btnSalvar, BorderLayout.SOUTH);
    }

    private void salvarAcao() {
        Paciente p = new Paciente();
        p.setNome(txtNome.getText());
        p.setCpf(txtCpf.getText());
        p.setCns(txtCns.getText());
        
        // IMPORTANTE: Converte para ISO antes de mandar para o banco
        p.setDataNascimento(MascaraUtil.paraISO(txtNascimento.getText()));
        
        p.setNomeMae(txtMae.getText());
        p.setCep(txtCep.getText()); // Agora a variável existe!
        p.setLogradouro(txtRua.getText());
        p.setNumero(txtNum.getText());
        p.setBairro(txtBairro.getText());
        p.setCidade(txtCidade.getText());
        p.setComplemento(txtComp.getText());
        p.setTelPrincipal(txtTel1.getText());
        p.setTelSecundario(txtTel2.getText());
        p.setTelFixo(txtTel3.getText());

        if (repo.salvar(p)) {
            JOptionPane.showMessageDialog(this, "Paciente salvo!");
            LogConfig.info("Paciente '" + p.getNome() + "' cadastrado com sucesso.");
            parent.atualizarTabela();
            dispose();
        } else {
            LogConfig.erro("Falha ao salvar paciente", new Exception("Erro de validação ou banco de dados")); 
            JOptionPane.showMessageDialog(this, "Erro ao salvar. Verifique CPF/CNS.");
        }
    }
}