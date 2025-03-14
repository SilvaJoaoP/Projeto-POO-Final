package service;

import java.util.List;

import dao.ExameDAO;
import exception.CampoObrigatorioException;
import exception.ExameNaoEncontradoException;
import exception.FormatoInvalidoException;
import model.Exame;

public class ExameService {

    private ExameDAO daoExame;
    
    public ExameService(ExameDAO dao) {
        this.daoExame = dao;
    }
    
    public void adicionarExame(Exame e) {
        if (e.getDescricao() == null || e.getDescricao().trim().isEmpty()) {
            throw new CampoObrigatorioException("Descrição");
        }
        
        if (e.getDataExame() == null || e.getDataExame().trim().isEmpty()) {
            throw new CampoObrigatorioException("Data do exame");
        }
        
        if (e.getPaciente() == null) {
            throw new CampoObrigatorioException("Paciente");
        }
        
        if (!e.getDataExame().matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new FormatoInvalidoException("Data do exame", "dd/mm/aaaa");
        }
        
        daoExame.add(e);
    }
    
    public Exame localizarExamePorId(Long id) {
        if (id == null) {
            throw new CampoObrigatorioException("ID do exame");
        }
        
        Exame exame = daoExame.findByID(id);
        if (exame == null) {
            throw new ExameNaoEncontradoException(id);
        }
        
        return exame;
    }
    
    public void deletarExame(Exame e) {
        if (e == null) {
            throw new IllegalArgumentException("Exame não pode ser nulo");
        }
        
        Exame existente = daoExame.findByID(e.getId());
        if (existente == null) {
            throw new ExameNaoEncontradoException(e.getId());
        }
        
        daoExame.delete(e);
    }
    
    public List<Exame> getExames(){
        return daoExame.getAll();
    }
    
    public void atualizarExame(Exame e) {
        if (e.getDescricao() == null || e.getDescricao().trim().isEmpty()) {
            throw new CampoObrigatorioException("Descrição");
        }
        
        if (e.getDataExame() == null || e.getDataExame().trim().isEmpty()) {
            throw new CampoObrigatorioException("Data do exame");
        }
        
        if (e.getPaciente() == null) {
            throw new CampoObrigatorioException("Paciente");
        }

        if (!e.getDataExame().matches("\\d{2}/\\d{2}/\\d{4}")) {
            throw new FormatoInvalidoException("Data do exame", "dd/mm/aaaa");
        }
        
        Exame existente = daoExame.findByID(e.getId());
        if (existente == null) {
            throw new ExameNaoEncontradoException(e.getId());
        }
        
        daoExame.update(e);
    }

    public List<Exame> getExamesPorPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new CampoObrigatorioException("ID do paciente");
        }
        
        if (daoExame instanceof ExameDAO) {
            return ((ExameDAO) daoExame).findByPaciente(pacienteId);
        }
        return null;
    }
}