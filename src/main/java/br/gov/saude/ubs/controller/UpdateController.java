package br.gov.saude.ubs.controller;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.file.*;
import javax.swing.JOptionPane;
import br.gov.saude.ubs.config.LogConfig;

public class UpdateController {
    private static final String VERSION_URL = "https://raw.githubusercontent.com/diogo202/sistema-ubs-modernizacao/refs/heads/main/version.txt";
    private static final String JAR_URL = "https://raw.githubusercontent.com/diogo202/sistema-ubs-modernizacao/refs/heads/main/target/sistema-ubs.jar";
    private static final String VERSAO_LOCAL = "1.0.0"; 

    public void verificarEAtualizar() {
        new Thread(() -> {
            try {
                LogConfig.info("Iniciando verificação de versão...");
                
                URL url = new URL(VERSION_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0"); 

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String versaoRemota = reader.readLine().trim();
                reader.close();

                if (!VERSAO_LOCAL.equals(versaoRemota)) {
                    int escolha = JOptionPane.showConfirmDialog(null, 
                        "Nova versão disponível (" + versaoRemota + "). Deseja atualizar e reiniciar agora?", 
                        "Atualização", JOptionPane.YES_NO_OPTION);

                    if (escolha == JOptionPane.YES_OPTION) {
                        baixarNovoJar(JAR_URL);
                        
                        File baixado = new File("sistema-novo.jar");
                        if (baixado.renameTo(new File("sistema-update-ready.jar"))) {
                            LogConfig.info("Arquivo pronto para substituição. Extraindo motor de atualização...");
                            extrairEExecutarBat();
                            System.exit(0);
                        }
                    }
                }
            } catch (Exception e) {
                LogConfig.erro("Erro no motor de atualização automática", e);
            }
        }).start();
    }

    private void baixarNovoJar(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); 

        try (InputStream in = conn.getInputStream()) {
            Files.copy(in, Paths.get("sistema-novo.jar"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void extrairEExecutarBat() throws IOException {
        InputStream is = getClass().getResourceAsStream("/update.bat");
        if (is == null) {
            LogConfig.erro("Arquivo update.bat não encontrado dentro do JAR (src/main/resources)!", null);
            return;
        }

        Path caminhoDestino = Paths.get("update.bat");
        Files.copy(is, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
        is.close();

        Runtime.getRuntime().exec("cmd /c start /min update.bat");
    }
}