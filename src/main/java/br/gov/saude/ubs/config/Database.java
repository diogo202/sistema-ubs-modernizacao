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
                 "cpf TEXT UNIQUE," +
                 "cns TEXT UNIQUE," +
                 "data_nascimento TEXT," +
                 "nome_mae TEXT," +
                 // Endereço
                 "logradouro TEXT," +
                 "numero TEXT," +
                 "bairro TEXT," +
                 "cidade TEXT," +
                 "complemento TEXT," +
                 // Telefones
                 "tel_principal TEXT," +
                 "tel_secundario TEXT," +
                 "tel_fixo TEXT," +
                 "data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP" +
                 ");";

    String sqlGestante = "CREATE TABLE IF NOT EXISTS gestantes (" +
                "id_gestante INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_paciente INTEGER NOT NULL," + // Chave estrangeira
                "data_abertura_prenatal TEXT NOT NULL," + 
                "dum TEXT," + // Data da Última Menstruação (comum em UBS)
                "idade_gestacional_inicial INTEGER," +
                "status TEXT DEFAULT 'ATIVO'," + // ATIVO ou CONCLUÍDO
                "FOREIGN KEY (id_paciente) REFERENCES pacientes(id)" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(sqlGestante);
        } catch (SQLException e) {
            LogConfig.erro("Erro ao criar tabela de pacientes", e);
        }
    }


}