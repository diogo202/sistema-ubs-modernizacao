package br.gov.saude.ubs.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.controller.UpdateController;

public class MainFrame extends JFrame {
    private JTable tabelaPacientes;
    private DefaultTableModel modeloTabela;
    private UpdateController updateController = new UpdateController();

    public MainFrame() {
        configurarJanela();
        inicializarComponentes();
        LogConfig.info("Interface principal carregada com sucesso.");
    }

    private void configurarJanela() {
        setTitle("Sistema de Modernização UBS - Jardim Itamaraty");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Painel Superior (Título e Info)
        JPanel painelSuperior = new JPanel();
        painelSuperior.setBackground(new Color(0, 102, 204));
        JLabel titulo = new JLabel("Gerenciamento de Pacientes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelSuperior.add(titulo);
        add(painelSuperior, BorderLayout.NORTH);

        // Tabela de Dados
        String[] colunas = {"ID", "Nome do Paciente", "CNS", "Data de Nasc."};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaPacientes = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaPacientes);
        add(scrollPane, BorderLayout.CENTER);

        // Painel Inferior (Botões)
        JPanel painelBotoes = new JPanel();
        JButton btnNovo = new JButton("Novo Paciente");
        JButton btnAtualizar = new JButton("Verificar Atualizações");
        
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnAtualizar.addActionListener(e -> {
        updateController.verificarEAtualizar();
        JOptionPane.showMessageDialog(this, "Verificação iniciada. Verifique o log para detalhes.");
        });
    }

    public void exibir() {
        setVisible(true);
    }
}