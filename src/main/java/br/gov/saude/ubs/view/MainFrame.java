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
        String[] colunas = {"ID", "Nome do Paciente", "CNS", "Data de Nasc.", "Telefone Principal"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPacientes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaPacientes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel Inferior
        JPanel painelBotoes = new JPanel();
        btnNovo = new JButton("Novo Paciente");
        btnVincularGestante = new JButton("Vincular Pré-Natal"); // Criado o botão aqui
        btnAtualizar = new JButton("Verificar Atualizações");
        
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnVincularGestante);
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);
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
                int idPaciente = Integer.parseInt(idObj.toString());

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

        // Ação Novo Paciente (Placeholder para o próximo passo)
        btnNovo.addActionListener(e -> {
            LogConfig.info("Abrindo formulário de novo paciente.");
            // FormularioPaciente form = new FormularioPaciente(this);
            // form.setVisible(true);
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
            modeloTabela.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCns(),
                p.getDataNascimento(),
                p.getTelPrincipal()
            });
        }
    }


    public void exibir() {
        setVisible(true);
    }
}