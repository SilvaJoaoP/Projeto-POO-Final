package service;

import java.util.List;
import dao.PacienteDAO;
import exception.*;
import model.Paciente;

public class PacienteService {

    private PacienteDAO daoPaciente;
    
    public PacienteService(PacienteDAO dao) {
        this.daoPaciente = dao;
    }
    
    public void adicionarPaciente(Paciente p) throws EntidadeDuplicadaException {

        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new CampoObrigatorioException("Nome");
        }
        
        if (p.getCpf() == null || p.getCpf().trim().isEmpty()) {
            throw new CampoObrigatorioException("CPF");
        }
        
        String cpfSemFormatacao = p.getCpf().replaceAll("[^0-9]", "");
        if (!cpfSemFormatacao.matches("\\d+")) {
            throw new FormatoInvalidoException("CPF", "apenas números");
        }

        Paciente pacienteExistente = findByCpf(p.getCpf());
        if (pacienteExistente != null) {
            throw new EntidadeDuplicadaException("Paciente", "CPF", p.getCpf());
        }
        
        daoPaciente.add(p);
    }
    
    public Paciente localizarPacientePorId(Long id) {
        if (id == null) {
            throw new CampoObrigatorioException("ID do paciente");
        }
        
        Paciente paciente = daoPaciente.findByID(id);
        if (paciente == null) {
            throw new PacienteNaoEncontradoException(id.toString());
        }
        
        return paciente;
    }
    
    public void deletarPaciente(Paciente p) {
        if (p == null) {
            throw new IllegalArgumentException("Paciente não pode ser nulo");
        }

        Paciente existente = daoPaciente.findByID(p.getId());
        if (existente == null) {
            throw new PacienteNaoEncontradoException(p.getCpf());
        }
        
        daoPaciente.delete(p);
    }
    
    public List<Paciente> getPacientes() {
        return daoPaciente.getAll();
    }
    
    public void atualizarPaciente(Paciente p) throws EntidadeDuplicadaException {
        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new CampoObrigatorioException("Nome");
        }
        
        if (p.getCpf() == null || p.getCpf().trim().isEmpty()) {
            throw new CampoObrigatorioException("CPF");
        }

        String cpfSemFormatacao = p.getCpf().replaceAll("[^0-9]", "");
        if (!cpfSemFormatacao.matches("\\d+")) {
            throw new FormatoInvalidoException("CPF", "apenas números");
        }

        Paciente existente = daoPaciente.findByID(p.getId());
        if (existente == null) {
            throw new PacienteNaoEncontradoException("ID: " + p.getId());
        }

        Paciente outroPaciente = findByCpf(p.getCpf());
        if (outroPaciente != null && !outroPaciente.getId().equals(p.getId())) {
            throw new EntidadeDuplicadaException("Paciente", "CPF", p.getCpf());
        }
        
        daoPaciente.update(p);
    }
    
    public Paciente findByCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new CampoObrigatorioException("CPF");
        }
        
        return daoPaciente.findByCpf(cpf);
    }
}