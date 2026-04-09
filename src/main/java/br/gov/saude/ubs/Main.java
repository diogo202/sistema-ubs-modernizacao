package br.gov.saude.ubs;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Inicia logs
        LogConfig.configurar();
        
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