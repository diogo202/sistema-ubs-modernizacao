package br.gov.saude.ubs.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.controller.UpdateController;


public class MainFrame extends JFrame {
    private JTable tabelaPacientes;
    private DefaultTableModel modeloTabela;
    private UpdateController updateController = new UpdateController();
    private br.gov.saude.ubs.repository.PacienteRepository pacienteRepo = new br.gov.saude.ubs.repository.PacienteRepository();

    // Declarando os botões como atributos para acessá-los em diferentes métodos
    private JButton btnNovo;
    private JButton btnAtualizar;
    private JButton btnVincularGestante;

    public MainFrame() {
        configurarJanela();
        inicializarComponentes();
        inicializarMenus();     // Chama a criação do menu
        configurarBotoesAcoes(); // Conecta os eventos
        atualizarTabela(); // Carrega os dados na tabela ao iniciar

        LogConfig.info("Interface principal carregada com sucesso.");
    }

    private void configurarJanela() {
        setTitle("Sistema de Modernização UBS - Jardim Itamaraty");
        setSize(900, 600); // Aumentei um pouco para caber as novas colunas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Painel Superior
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(new Color(0, 102, 204));
        JLabel titulo = new JLabel("Gerenciamento de Pacientes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelSuperior.add(titulo);
        add(painelSuperior, BorderLayout.NORTH);

        // Tabela de Dados - Adicionada a coluna de Telefone para visualização rápida
        String[] colunas = {"ID", "Nome do Paciente", "CNS", "Idade", "Telefone Principal"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPacientes = new JTable(modeloTabela);

        // Esconde a coluna ID (índice 0) mantendo os dados acessíveis via código
        tabelaPacientes.getColumnModel().getColumn(0).setMinWidth(0);
        tabelaPacientes.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelaPacientes.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(tabelaPacientes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel Inferior (Botões + Rodapé de Versão)
        JPanel painelInferior = new JPanel(new BorderLayout()); // Mudamos para BorderLayout
        
        // Sub-painel para os botões
        JPanel painelBotoes = new JPanel();
        btnNovo = new JButton("Novo Paciente");
        btnVincularGestante = new JButton("Vincular Pré-Natal");
        btnAtualizar = new JButton("Verificar Atualizações");
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnVincularGestante);
        painelBotoes.add(btnAtualizar);
        
        // Label da Versão no rodapé
        String versaoAtual = carregarVersao(); 
        JLabel lblVersao = new JLabel(" Versão: " + versaoAtual + "  ");
        lblVersao.setFont(new Font("Arial", Font.ITALIC, 10));
        
        // O painel de botões ficará à esquerda e o label de versão à direita
        painelInferior.add(painelBotoes, BorderLayout.CENTER);
        painelInferior.add(lblVersao, BorderLayout.EAST); // Alinhado à direita no rodapé

        add(painelInferior, BorderLayout.SOUTH);
    }

    private void inicializarMenus() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuCadastro = new JMenu("Cadastros");
        JMenuItem itemPacientes = new JMenuItem("Pacientes");
        menuCadastro.add(itemPacientes);
        
        JMenu menuProgramas = new JMenu("Programas Saúde");
        JMenuItem itemGestantes = new JMenuItem("Acompanhamento de Gestantes");
        menuProgramas.add(itemGestantes);
        
        menuBar.add(menuCadastro);
        menuBar.add(menuProgramas);
        setJMenuBar(menuBar);
    }

    private void configurarBotoesAcoes() {
        // Ação de Atualização
        btnAtualizar.addActionListener(e -> {
            LogConfig.info("Botão de atualização pressionado.");
            updateController.verificarEAtualizar();
        });

        // Ação de Vínculo de Gestante
        btnVincularGestante.addActionListener(e -> {
            int linhaSelecionada = tabelaPacientes.getSelectedRow();
        
            if (linhaSelecionada == -1) {
                LogConfig.info("Tentativa de vínculo de gestante sem paciente selecionado.");
                JOptionPane.showMessageDialog(this, "Por favor, selecione um paciente na tabela para iniciar o Pré-Natal.");
                return;
            }

            try {
                Object idObj = tabelaPacientes.getValueAt(linhaSelecionada, 0);
                String nomePaciente = (String) tabelaPacientes.getValueAt(linhaSelecionada, 1);
                int idPaciente = (idObj != null) ? Integer.parseInt(idObj.toString()) : 0;

                LogConfig.info("Abertura de diálogo de vínculo para Paciente: " + nomePaciente);

                String dataAbertura = JOptionPane.showInputDialog(this, 
                    "Paciente: " + nomePaciente + "\nInforme a Data de Abertura do Pré-Natal (DD/MM/AAAA):",
                    "Abertura de Pré-Natal",
                    JOptionPane.QUESTION_MESSAGE);

                if (dataAbertura != null && !dataAbertura.isEmpty()) {
                    LogConfig.info("Iniciando registro de Pré-Natal. Data: " + dataAbertura + " | ID Paciente: " + idPaciente);
                    
                    // Aqui entrará o seu Repository no próximo passo
                    JOptionPane.showMessageDialog(this, "Vínculo de Pré-Natal registrado com sucesso!");
                    LogConfig.info("Vínculo registrado com sucesso para ID: " + idPaciente);
                } else {
                    LogConfig.info("Usuário cancelou a inserção da data de abertura.");
                }
            } catch (Exception ex) {
                LogConfig.erro("Erro ao processar dados da tabela no vínculo", ex);
                JOptionPane.showMessageDialog(this, "Erro interno ao selecionar paciente.");
            }
        });

        // Ação Novo Paciente
        btnNovo.addActionListener(e -> {
            LogConfig.info("Abrindo formulário de novo paciente.");
            FormularioPaciente form = new FormularioPaciente(this);
            form.setVisible(true);
        });
    }

    public void atualizarTabela() {
        LogConfig.info("Atualizando visualização da tabela de pacientes...");
    
        // Limpa a tabela atual
        modeloTabela.setRowCount(0);

        // Busca os dados no banco
        java.util.List<br.gov.saude.ubs.model.Paciente> pacientes = pacienteRepo.buscarTodos();
        
        // Preenche o modelo da tabela
        for (br.gov.saude.ubs.model.Paciente p : pacientes) {
            String idadeStr = calcularIdadeSimples(p.getDataNascimento());
            modeloTabela.addRow(new Object[]{
                p.getId(),          // Coluna 0 (será oculta)
                p.getNome(),        // Coluna 1
                p.getCns(),         // Coluna 2
                idadeStr,           // Coluna 3 (Idade calculada)
                p.getTelPrincipal() // Coluna 4 (Telefone Principal)
            });
        }
    }

    // Método auxiliar no MainFrame
    private String calcularIdadeSimples(String dataNasc) {
        if (dataNasc == null || dataNasc.isEmpty()) return "--";
        try {
            // Agora tenta ler o formato YYYY-MM-DD que virá do banco
            java.time.LocalDate nasc = java.time.LocalDate.parse(dataNasc);
            return String.valueOf(java.time.temporal.ChronoUnit.YEARS.between(nasc, java.time.LocalDate.now()));
        } catch (Exception e) {
            // Se falhar, tenta o formato BR caso ainda existam dados antigos
            try {
                java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate nasc = java.time.LocalDate.parse(dataNasc, fmt);
                return String.valueOf(java.time.temporal.ChronoUnit.YEARS.between(nasc, java.time.LocalDate.now()));
            } catch (Exception ex) {
                return "??";
            }
        }
    }

    private String carregarVersao() {
        java.util.Properties prop = new java.util.Properties();
        try (java.io.InputStream input = getClass().getResourceAsStream("/version.properties")) {
            if (input != null) {
                prop.load(input);
                return prop.getProperty("version", "v2.8.4-dev");
            }
        } catch (java.io.IOException ex) {
            LogConfig.erro("Erro ao carregar version.properties", ex);
        }
        return "v2.8.4-dev"; // Retorna o fallback se o input for null ou der erro
    }

    public void exibir() {
        setVisible(true);
    }
}