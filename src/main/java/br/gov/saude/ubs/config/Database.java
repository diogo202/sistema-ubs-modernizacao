package br.gov.saude.ubs.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // Definimos a pasta e o nome do arquivo
    private static final String NOME_PASTA = "database";
    private static final String NOME_BANCO = "ubs_dados.db";
    private static final String URL = "jdbc:sqlite:" + NOME_PASTA + "/" + NOME_BANCO;

    public static Connection conectar() {
        try {
            prepararDiretorio(); // Garante que a pasta existe antes da conexão
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            criarTabelas(conn);
            return conn;
        } catch (Exception e) {
            LogConfig.erro("Erro na conexão/criação do banco", e);
            return null;
        }
    }

    private static void prepararDiretorio() {
        File diretorio = new File(NOME_PASTA);
        if (!diretorio.exists()) {
            boolean criado = diretorio.mkdirs();
            if (criado) {
                LogConfig.info("Pasta de banco de dados criada: " + NOME_PASTA);
            }
        }
    }

    private static void criarTabelas(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS pacientes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "cns TEXT," +
                     "data_nascimento TEXT" +
                     ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            LogConfig.erro("Erro ao criar tabela de pacientes", e);
        }
    }
}