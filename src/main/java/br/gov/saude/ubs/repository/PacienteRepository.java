package br.gov.saude.ubs.repository;

import br.gov.saude.ubs.config.Database;
import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.model.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacienteRepository {

    public boolean salvar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (nome, cns, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getCns());
            pstmt.setString(3, paciente.getDataNascimento());

            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                LogConfig.info("Paciente salvo com sucesso: " + paciente.getNome());
                return true;
            }

        } catch (SQLException e) {
            LogConfig.erro("Erro ao salvar paciente no banco de dados", e);
        }
        return false;
    }
}