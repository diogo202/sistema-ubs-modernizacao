package br.gov.saude.ubs.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class LogConfig {
    private static final Logger logger = Logger.getLogger("UBS_LOG");

    public static void configurar() {
        try {
            // Cria pasta de logs se não existir
            File folder = new File("logs");
            if (!folder.exists()) folder.mkdir();

            // Configura arquivo de log (ubs_sistema.log) - mantém os últimos 5 arquivos de 1MB
            FileHandler fh = new FileHandler("logs/ubs_sistema.log", 1048576, 5, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);

            // Configura log no console
            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new SimpleFormatter());
            logger.addHandler(ch);

            logger.info("Sistema de Log iniciado com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao configurar logs: " + e.getMessage());
        }
    }

    // Método estático - acessado via LogConfig.info()
    public static void info(String mensagem) {
        logger.info(mensagem);
    }

    // Método estático para erros - aceita mensagem e exceção (opcional)
    public static void erro(String mensagem, Exception e) {
        logger.log(Level.SEVERE, mensagem + " | Erro: " + e.getMessage(), e);
    }
}