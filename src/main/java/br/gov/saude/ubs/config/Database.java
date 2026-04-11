package br.gov.saude.ubs.config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String NOME_PASTA = "database";
    private static final String NOME_BANCO = "ubs_dados.db";
    private static final String URL = "jdbc:sqlite:" + NOME_PASTA + "/" + NOME_BANCO;

    // Renomeado de conectar() para getConnection() para bater com os Repositories
    public static Connection getConnection() {
        try {
            prepararDiretorio(); 
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            criarTabelas(conn);
            return conn;
        } catch (Exception e) {
            // Se o LogConfig ainda não estiver disponível no escopo, use System.err
            System.err.println("Erro na conexão com o SQLite: " + e.getMessage());
            return null;
        }
    }

    private static void prepararDiretorio() {
        File diretorio = new File(NOME_PASTA);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    private static void criarTabelas(Connection conn) {
        String sqlPacientes = "CREATE TABLE IF NOT EXISTS pacientes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "cpf TEXT UNIQUE," +
                     "cns TEXT UNIQUE," +
                     "data_nascimento TEXT," +
                     "nome_mae TEXT," +
                     "logradouro TEXT," +
                     "numero TEXT," +
                     "bairro TEXT," +
                     "cidade TEXT," +
                     "complemento TEXT," +
                     "tel_principal TEXT," +
                     "tel_secundario TEXT," +
                     "tel_fixo TEXT," +
                     "data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP" +
                     ");";

        String sqlGestante = "CREATE TABLE IF NOT EXISTS gestantes (" +
                    "id_gestante INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_paciente INTEGER NOT NULL," +
                    "data_abertura_prenatal TEXT NOT NULL," + 
                    "dum TEXT," + 
                    "idade_gestacional_inicial INTEGER," +
                    "status TEXT DEFAULT 'ATIVO'," + 
                    "FOREIGN KEY (id_paciente) REFERENCES pacientes(id)" +
                    ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlPacientes);
            stmt.execute(sqlGestante);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}