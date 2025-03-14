package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Exame;
import model.Paciente;
import dao.PacienteDAO;

public class ExameDAO implements GenericDAO<Exame, Long> {

    private DatabaseConnection db;
    private PacienteDAO pacienteDAO;
    
    public ExameDAO(DatabaseConnection db) {
        this.db = db;
        this.pacienteDAO = new PacienteDAO(db);
    }
    
    @Override
    public void add(Exame obj) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("INSERT INTO prontuario.exames (descricao, data, pacientes_id) VALUES (?,?,?)")) {
            pstm.setString(1, obj.getDescricao());
            pstm.setString(2, obj.getDataExame());
            pstm.setLong(3, obj.getPaciente().getId());
            
            pstm.executeUpdate();
            System.out.println("Exame adicionado para paciente ID: " + obj.getPaciente().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Exame findByID(Long id) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM prontuario.exames WHERE id = ?")) {
            pstm.setLong(1, id);
            
            try(ResultSet rs = pstm.executeQuery()) {
                if(rs.next()) {
                    Exame exame = new Exame(
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        rs.getString("data") 
                    );
                    
                    Long pacienteId = rs.getLong("pacientes_id"); 
                    Paciente paciente = pacienteDAO.findByID(pacienteId);
                    exame.setPaciente(paciente);
                    
                    return exame;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Exame obj) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM prontuario.exames WHERE id = ?")) {
            pstm.setLong(1, obj.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Exame obj) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("UPDATE prontuario.exames SET descricao = ?, data = ?, pacientes_id = ? WHERE id = ?")) {
            pstm.setString(1, obj.getDescricao());
            pstm.setString(2, obj.getDataExame());
            pstm.setLong(3, obj.getPaciente().getId());
            pstm.setLong(4, obj.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Exame> getAll() {
        List<Exame> exames = new ArrayList<>();
        
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM prontuario.exames");
            ResultSet rs = pstm.executeQuery()) {
            
            while(rs.next()) {
                Exame exame = new Exame(
                    rs.getLong("id"),
                    rs.getString("descricao"),
                    rs.getString("data") 
                );
                
                Long pacienteId = rs.getLong("pacientes_id"); 
                Paciente paciente = pacienteDAO.findByID(pacienteId);
                exame.setPaciente(paciente);
                
                exames.add(exame);
            }
            
            return exames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new ArrayList<>();
    }
    
    public List<Exame> findByPaciente(Long pacienteId) {
        List<Exame> exames = new ArrayList<>();
        
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM prontuario.exames WHERE pacientes_id = ?")) {
            pstm.setLong(1, pacienteId);
            
            try(ResultSet rs = pstm.executeQuery()) {
                while(rs.next()) {
                    Exame exame = new Exame(
                        rs.getLong("id"),
                        rs.getString("descricao"),
                        rs.getString("data")  
                    );

                    Paciente paciente = pacienteDAO.findByID(pacienteId);
                    exame.setPaciente(paciente);
                    
                    exames.add(exame);
                }
                
                return exames;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new ArrayList<>();
    }
}