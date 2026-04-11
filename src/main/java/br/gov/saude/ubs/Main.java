package br.gov.saude.ubs;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.model.Paciente;
import br.gov.saude.ubs.repository.PacienteRepository;
import br.gov.saude.ubs.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Inicia logs
        LogConfig.configurar();

        // Teste rápido de inserção
        Paciente p = new Paciente();
        p.setNome("Maria Oliveira");
        p.setCpf("123.456.789-00");
        p.setCns("987654321000000");
        p.setLogradouro("Rua das Flores");
        p.setNumero("123");
        p.setTelPrincipal("(14) 99999-8888"); // Celular
        p.setTelSecundario("(14) 98888-7777"); // Recado
        p.setTelFixo("(14) 3322-1100");       // Fixo
        
        PacienteRepository repo = new PacienteRepository();
        repo.salvar(p);
        
        // No Swing, a interface deve ser iniciada na "Event Dispatch Thread"
        SwingUtilities.invokeLater(() -> {
            try {
                // Tenta usar o visual nativo do Windows
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                MainFrame telaPrincipal = new MainFrame();
                telaPrincipal.exibir();
                
            } catch (Exception e) {
                LogConfig.erro("Falha ao iniciar interface gráfica", e);
            }
        });
    }
}