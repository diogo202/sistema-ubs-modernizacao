package br.gov.saude.ubs.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import br.gov.saude.ubs.config.Database;
import br.gov.saude.ubs.config.LogConfig;

public class GestanteRepository {

    public boolean vincularGestante(int idPaciente, String dataAbertura) {
        String sql = "INSERT INTO gestantes (id_paciente, data_abertura_prenatal, status) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPaciente);
            pstmt.setString(2, dataAbertura);
            pstmt.setString(3, "ATIVO");

            pstmt.executeUpdate();
            LogConfig.info("Vínculo de Pré-Natal criado para Paciente ID: " + idPaciente + " em " + dataAbertura);
            return true;

        } catch (SQLException e) {
            LogConfig.erro("Erro ao vincular gestante no banco de dados", e);
            return false;
        }
    }
}