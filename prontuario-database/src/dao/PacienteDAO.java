package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Paciente;

public class PacienteDAO implements GenericDAO<Paciente, Long>{

	private DatabaseConnection db;
	
	public PacienteDAO(DatabaseConnection db) {
		this.db = db;
	}
	
	@Override
	public void add(Paciente obj) {
		try(PreparedStatement pstm = db.getConnection()
				.prepareStatement("INSERT INTO PACIENTES VALUES (?,?,?)")){
			pstm.setLong(1, 0);
			pstm.setString(2, obj.getCpf());
			pstm.setString(3, obj.getNome());
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Paciente findByID(Long id) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM PACIENTES WHERE id = ?")) {
            pstm.setLong(1, id);
            try(ResultSet rs = pstm.executeQuery()) {
                if(rs.next()) {
                    return new Paciente(
                        rs.getLong("id"),
                        rs.getString("cpf"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public void delete(Paciente obj) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM PACIENTES WHERE id = ?")) {
            pstm.setLong(1, obj.getId());
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void update(Paciente obj) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("UPDATE PACIENTES SET cpf = ?, nome = ? WHERE id = ?")) {
            pstm.setString(1, obj.getCpf());
            pstm.setString(2, obj.getNome());
            pstm.setLong(3, obj.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public List<Paciente> getAll() {
		List<Paciente> temp = new ArrayList<>();
		
		try(PreparedStatement pstm = db.getConnection().prepareStatement("SELECT * FROM PACIENTES;"); 
				ResultSet rs = pstm.executeQuery()){
			while(rs.next()) {
				Paciente p = new Paciente(rs.getLong("id"), rs.getString("cpf"), rs.getString("nome"));
				temp.add(p);
			}
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Paciente findByCpf(String cpf) {
        try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM PACIENTES WHERE cpf = ?")) {
            pstm.setString(1, cpf);
            try(ResultSet rs = pstm.executeQuery()) {
                if(rs.next()) {
                    return new Paciente(
                        rs.getLong("id"),
                        rs.getString("cpf"),
                        rs.getString("nome")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}