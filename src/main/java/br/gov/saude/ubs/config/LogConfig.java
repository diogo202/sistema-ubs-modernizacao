package br.gov.saude.ubs.config;

import java.io.IOException;
import java.util.logging.*;

public class LogConfig {
    private static final Logger logger = Logger.getLogger("UBS_LOG");

    public static void configurar() {
        try {
            // Define o arquivo de log e permite "append" (não apaga o anterior)
            FileHandler fh = new FileHandler("sistema_ubs.log", true);
            fh.setFormatter(new SimpleFormatter()); // Formato legível por humanos
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Não foi possível iniciar o arquivo de log: " + e.getMessage());
        }
    }

    public static void info(String msg) { logger.info(msg); }
    public static void erro(String msg, Exception e) { logger.log(Level.SEVERE, msg, e); }
}