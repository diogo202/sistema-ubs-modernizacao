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
    
    private JLabel lblIdadeDinamica; // Label para mostrar a idade calculada

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
        
        // Aba 1: Dados Pessoais - Aumentamos para 7 linhas para acomodar tudo em pares
        JPanel pnlDados = new JPanel(new GridLayout(7, 2, 10, 10)); 
        pnlDados.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlDados.add(new JLabel("Nome Completo:")); 
        txtNome = new JTextField(); pnlDados.add(txtNome);

        pnlDados.add(new JLabel("CPF:")); 
        txtCpf = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CPF)); pnlDados.add(txtCpf);

        pnlDados.add(new JLabel("CNS:")); 
        txtCns = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_CNS)); pnlDados.add(txtCns);

        pnlDados.add(new JLabel("Data Nasc (DD/MM/AAAA):")); 
        txtNascimento = new JFormattedTextField(MascaraUtil.aplicar(MascaraUtil.MASCARA_DATA)); pnlDados.add(txtNascimento);

        // Label que será atualizado via código
        pnlDados.add(new JLabel("Idade Calculada:"));
        lblIdadeDinamica = new JLabel("-- anos"); 
        lblIdadeDinamica.setForeground(new Color(0, 102, 204));
        lblIdadeDinamica.setFont(new Font("Arial", Font.BOLD, 13));
        pnlDados.add(lblIdadeDinamica); // Adiciona o label dinâmico para mostrar a idade

        pnlDados.add(new JLabel("Nome da Mãe:")); 
        txtMae = new JTextField(); pnlDados.add(txtMae);

        // Campo vazio para manter o alinhamento do grid (paridade)
        pnlDados.add(new JLabel("")); 
        pnlDados.add(new JLabel(""));

        txtNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String dataTexto = txtNascimento.getText().replace("_", "").replace("/", "").trim();
                if (dataTexto.length() == 8) {
                    try {
                        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        java.time.LocalDate nasc = java.time.LocalDate.parse(txtNascimento.getText(), fmt);
                        long idade = java.time.temporal.ChronoUnit.YEARS.between(nasc, java.time.LocalDate.now());
                
                        // Atualiza apenas o label dinâmico
                        lblIdadeDinamica.setText(idade + " anos");
                        LogConfig.info("Idade calculada para " + txtNome.getText() + ": " + idade);
                    } catch (Exception e) {
                        lblIdadeDinamica.setText("Data inválida");
                    }
                }
            }
        });
        
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