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
        br.gov.saude.ubs.model.Paciente p = new br.gov.saude.ubs.model.Paciente();
        p.setNome("Maria da Silva (Teste)");
        p.setCpf("000.000.000-01");
        p.setCns("700000000000001");
        p.setTelPrincipal("(14) 99999-0000");
        
        new br.gov.saude.ubs.repository.PacienteRepository().salvar(p);
        
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