package service;

import java.util.List;


import dao.PacienteDAO;
import model.Paciente;

public class PacienteService {

	private PacienteDAO daoPaciente; // Alterado para PacienteDAO
    
    public PacienteService(PacienteDAO dao) { // Tipo do parâmetro ajustado
        this.daoPaciente = dao;
	}
	
	public void adicionarPaciente(Paciente p) {
		daoPaciente.add(p);
	}
	
	public Paciente localizarPacientePorId(Long id) {
		return daoPaciente.findByID(id);
	}
	
	
	public void deletarPaciente(Paciente p) {
		daoPaciente.delete(p);
	}
	
	public List<Paciente> getPacientes(){
		return daoPaciente.getAll();
	}
	
	public void atualizarPaciente(Paciente p) {
		daoPaciente.update(p);
	}
	
	public Paciente findByCpf(String cpf) {
        return daoPaciente.findByCpf(cpf); 
    }
	
	
}