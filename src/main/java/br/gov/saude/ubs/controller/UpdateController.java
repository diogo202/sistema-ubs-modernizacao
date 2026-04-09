package br.gov.saude.ubs.controller;

import java.io.*;
import java.net.URL;
import java.nio.file.*;

import javax.swing.JOptionPane;

import br.gov.saude.ubs.config.LogConfig;

public class UpdateController {
    // Substitua pela URL do seu arquivo de versão no GitHub/Gist (Raw)
    private static final String VERSION_URL = "https://raw.githubusercontent.com/diogo202/sistema-ubs-modernizacao/refs/heads/main/version.txt";
    private static final String JAR_URL = "https://github.com/diogo202/sistema-ubs-modernizacao/raw/refs/heads/main/target/sistema-ubs-modernizacao-1.0-SNAPSHOT-jar-with-dependencies.jar";
    private static final String VERSAO_LOCAL = "1.0.0"; 

    public void verificarEAtualizar() {
    new Thread(() -> {
        try {
            LogConfig.info("Iniciando verificação de versão...");
            // Adicionamos um User-Agent para o GitHub não bloquear a requisição
            URL url = new URL(VERSION_URL);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); 

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String versaoRemota = reader.readLine().trim();
            reader.close();

            if (!VERSAO_LOCAL.equals(versaoRemota)) {
                // Código de download que já fizemos...
                LogConfig.info("Nova versão disponível: " + versaoRemota);
                baixarNovoJar(JAR_URL);
                criarScriptSubstituicao();
                
                JOptionPane.showMessageDialog(null, 
                    "Uma nova atualização foi baixada!\nO sistema será fechado para aplicar as mudanças.", 
                    "Atualização Disponível", JOptionPane.INFORMATION_MESSAGE);
                
                // Fecha o sistema e roda o .bat
                Runtime.getRuntime().exec("cmd /c start update.bat");
                System.exit(0);
            }
        } catch (Exception e) {
            LogConfig.erro("Erro no motor de atualização", e);
        }
    }).start();
}

    private String baixarTexto(String urlString) throws Exception {
        URL url = new URL(urlString);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return reader.readLine();
        }
    }

    private void baixarNovoJar(String urlString) throws Exception {
        URL url = new URL(urlString);
        try (InputStream in = url.openStream()) {
            // Salva como um arquivo temporário
            Files.copy(in, Paths.get("sistema-novo.jar"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void criarScriptSubstituicao() throws IOException {
        // Cria um .bat que espera o programa fechar, troca os arquivos e reabre o sistema
        String script = "@echo off\n" +
                        "timeout /t 3 /nobreak > nul\n" + // Espera 3 segundos
                        "del sistema-ubs.jar\n" +
                        "ren sistema-novo.jar sistema-ubs.jar\n" +
                        "start javaw -jar sistema-ubs.jar\n" +
                        "del update.bat";
        
        Files.write(Paths.get("update.bat"), script.getBytes());
    }
}