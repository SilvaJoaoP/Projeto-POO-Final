package service;

import java.util.List;

import dao.ExameDAO;
import model.Exame;

public class ExameService {

    private ExameDAO daoExame;
    
    public ExameService(ExameDAO dao) {
        this.daoExame = dao;
    }
    
    public void adicionarExame(Exame e) {
        daoExame.add(e);
    }
    
    public Exame localizarExamePorId(Long id) {
        return daoExame.findByID(id);
    }
    
    public void deletarExame(Exame e) {
        daoExame.delete(e);
    }
    
    public List<Exame> getExames(){
        return daoExame.getAll();
    }
    
    public void atualizarExame(Exame e) {
        daoExame.update(e);
    }

    public List<Exame> getExamesPorPaciente(Long pacienteId) {
        if (daoExame instanceof ExameDAO) {
            return ((ExameDAO) daoExame).findByPaciente(pacienteId);
        }
        return null;
    }
}