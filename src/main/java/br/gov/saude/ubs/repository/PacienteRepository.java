package br.gov.saude.ubs.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.gov.saude.ubs.config.Database;
import br.gov.saude.ubs.config.LogConfig;
import br.gov.saude.ubs.model.Paciente;

public class PacienteRepository {

    public boolean salvar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (" +
                     "nome, cpf, cns, data_nascimento, nome_mae, " +
                     "logradouro, numero, bairro, cidade, complemento, " +
                     "tel_principal, tel_secundario, tel_fixo" +
                     ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paciente.getNome());
            pstmt.setString(2, paciente.getCpf());
            pstmt.setString(3, paciente.getCns());
            pstmt.setString(4, paciente.getDataNascimento());
            pstmt.setString(5, paciente.getNomeMae());
            
            // Endereço
            pstmt.setString(6, paciente.getLogradouro());
            pstmt.setString(7, paciente.getNumero());
            pstmt.setString(8, paciente.getBairro());
            pstmt.setString(9, paciente.getCidade());
            pstmt.setString(10, paciente.getComplemento());
            
            // Telefones
            pstmt.setString(11, paciente.getTelPrincipal());
            pstmt.setString(12, paciente.getTelSecundario());
            pstmt.setString(13, paciente.getTelFixo());

            pstmt.executeUpdate();
            LogConfig.info("Paciente salvo com sucesso: " + paciente.getNome() + " (CPF: " + paciente.getCpf() + ")");
            return true;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                LogConfig.erro("Erro: CPF ou CNS já cadastrado no sistema.", e);
            } else {
                LogConfig.erro("Erro ao salvar paciente no banco de dados", e);
            }
            return false;
        }
    }

    public List<Paciente> buscarTodos() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, cns, data_nascimento, tel_principal FROM pacientes ORDER BY nome ASC";
    
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCns(rs.getString("cns"));
                p.setDataNascimento(rs.getString("data_nascimento"));
                p.setTelPrincipal(rs.getString("tel_principal"));
                lista.add(p);
            }
        } catch (SQLException e) {
            LogConfig.erro("Erro ao buscar pacientes", e);
        }
        return lista;
    }
}